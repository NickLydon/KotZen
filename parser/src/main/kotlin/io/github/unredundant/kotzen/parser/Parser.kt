package io.github.unredundant.kotzen.parser

/**
 * Input to the parser
 */
interface Parseable {
    /**
     * Consumes input by a number of characters
     * @return The remaining input after consumption
     */
    fun advance(steps: Int) : Parseable

    /**
     * @return The number of characters that remain unparsed
     */
    fun remaining() : Int

    /**
     * @return The next character
     */
    fun head() : Char

    /**
     * @return Whether the end of the input has been reached
     */
    fun isEmpty() = remaining() == 0

    /**
     * @return The unparsed input
     */
    fun unparsed() : String
}

/**
 * A parser returns the remaining unparsed input and the typed result of parsing the input
 */
typealias ParserOutput<T> = Pair<Parseable, T>

/**
* A parser takes an input, which is parseable, and returns a typed output
 */
typealias Parser<T> = (Parseable) -> ParserOutput<T>?

/**
 * Wraps a string with a pointer to the head of the remaining unparsed input
 * to prevent repeatedly creating substrings
 */
data class StringParseable(private val span: Span) : Parseable {
    override fun advance(steps: Int): Parseable = StringParseable(span.substring(steps))
    override fun remaining(): Int = span.length
    override fun head(): Char = span.value[span.start]
    override fun unparsed(): String = span.value.substring(span.start)
}

/**
 * Helper function to wrap a string for input to a parser
 * @param index The start of the unparsed input
 * @return The wrapped string to input to a parser
 */
fun String.parseable(index : Int = 0) = StringParseable(Span(this, index))

/**
 * Helper function to actually run the parser
 * @param input The string to be parsed
 * @return The typed result of parsing
 * @throws IllegalStateException When unparsed input remains, or cannot be parsed at all
 */
fun <T> Parser<T>.parse(input: String): T {
    val output = this(input.parseable()) ?: error("Invalid input: \n$input")

    if (!output.first.isEmpty()) error("Unconsumed input: " + output.first.unparsed())

    return output.second
}

/**
 * Parses a character successfully if the given predicate returns true
 * @param fn Predicate that must be satisfied
 */
fun sat(fn: (Char) -> Boolean) = { input: Parseable ->
    if (input.isEmpty() || !fn(input.head())) null
    else Pair(input.advance(1), input.head())
}

/**
 * Maps a parser output to a new value
 * @param fn A function to transform the parser output
 */
fun <S, T> Parser<S>.map(fn: (S) -> T): Parser<T> = { input: Parseable ->
    val output = this(input)
    if (output == null) null
    else Pair(output.first, fn(output.second))
}

/**
 * Maps a parser output to a new value
 * @param value The new value to return
 */
fun <S, T> Parser<S>.mapTo(value: T): Parser<T> = this.map { value }

/**
 * Returns a parser that always succeeds.
 * The result is the unconsumed input and the value passed to this function
 */
fun <T> pure(default: T) = { input: Parseable ->
    Pair(input, default)
}

/**
 * Returns a parser that always fails
 */
fun <T> fail(): Parser<T> = { null }

/**
 * A parser that consumes any character
 */
val char = sat { true }

/**
 * A parser that consumes the given character
 */
fun char(c: Char) = sat { it == c }

/**
 * Returns a parser that joins a parsed collection of characters to a string
 */
fun Parser<Iterable<Char>>.text() = this.map { it.joinToString("") }

/**
 * A parser that consumes a digit
 */
val digit = sat(Char::isDigit)

/**
 * A parser that consumes at least one digit to create a number
 */
val number = digit.atLeastOne().map { it.joinToString("").toInt() }

/**
 * A parser that consumes a positive or negative whole number
 */
val integer =
    char('-').bind {
        number.map(Math::negateExact)
    }.or(number)

/**
 * A parser that consumes a decimal number
 */
val decimal =
    integer.skipRight(char('.')).bind { i -> number.map { d -> "$i.$d".toDouble() } }
        .or(integer.map(Int::toDouble))

/**
 * A parser that consumes a whitespace character according to the unicode standard
 */
val whitespace = sat(Char::isWhitespace)

/**
 * Consumes the whitespace around a given parser
 */
fun <T> Parser<T>.token() = this.between(whitespace.many())

/**
 * A parser that consumes a letter
 */
val alpha = sat(Char::isLetter)

/**
 * Returns a parser that returns the first successful result
 * @param alternative The parser to try if this one fails
 * @return The result from this parser, or failing that the alternative
 */
fun <T> Parser<T>.or(alternative: Parser<T>) = { input: Parseable ->
    this(input) ?: alternative(input)
}

/**
 * Returns a parser that returns the first successful result
 */
fun <T> Iterable<Parser<T>>.first() = this.fold(fail<T>()) { a, b -> a.or(b) }

/**
 * Returns a parser that consumes 0 or more
 */
fun <T> Parser<T>.many(): Parser<Iterable<T>> =
    this.atLeastOne().or(pure(listOf()))

/**
 * Returns a parser that consumes 1 or more
 */
fun <T> Parser<T>.atLeastOne() =
    this.bind { this.many().map { xs -> listOf(it) + xs } }

/**
 * Flattens multiple parsers (called flatMap elsewhere)
 * @param fn Given the result of the current parser, return a new parser
 */
fun <T, S> Parser<T>.bind(fn: (T) -> Parser<S>) = { input: Parseable ->
    val output = this(input)
    if (output == null) null
    else fn(output.second)(output.first)
}

/**
 * Returns a parser for input separated by a delimiter, e.g. 1,2,3
 * @param delimiter The parser for the delimiter, e.g. char(',') for a CSV file
 */
fun <T, S> Parser<T>.delimitedBy(delimiter: Parser<S>) =
    this.bind { delimiter.skipLeft(this).many().map { xs -> listOf(it) + xs } }

/**
 * Returns a parser for input surrounded by a pair of parsers
 * @param p The parser to the left, and the parser to the right
 */
fun <T, S, U> Parser<T>.between(p: Pair<Parser<S>, Parser<U>>) = this.between(p.first, p.second)

/**
 * Returns a parser for input surrounded the same parser to the left and to the right
 * @param p The parser to the left, and the parser to the right
 */
fun <T, S> Parser<T>.between(p: Parser<S>) = this.between(p, p)

/**
 * Returns a parser for input surrounded by a pair of parsers
 * @param left The parser to the left of the input
 * @param right The parser to the right of the input
 */
fun <T, S, U> Parser<T>.between(left: Parser<S>, right: Parser<U>) =
    left.skipLeft(this.skipRight(right))

/**
 * Returns a parser that ignores the successful result of this
 * @param next The parser whose result will be returned
 */
fun <T, S> Parser<T>.skipLeft(next: Parser<S>) = this.bind { next }

/**
 * Returns a parser that uses this parser's output, ignoring the successful result of the next
 * @param next The parser whose result will be ignored
 */
fun <T, S> Parser<T>.skipRight(next: Parser<S>) = this.bind { next.mapTo(it) }

/**
 * Returns a parser that consumes a given string
 * @throws IllegalStateException When value is empty
 * @param value The string to match
 */
fun symbol(value: String): Parser<String> = symbol(Span(value))

/**
 * Returns a parser that consumes a given string. This overload can be used to avoid string allocations
 * @throws IllegalStateException When value is empty
 * @param value The string/substring to match
 */
fun symbol(value: Span): Parser<String> =
    if (value.length == 0) pure("")
    else char(value.value[value.start]).bind { symbol(value.substring(1)).map { xs -> it + xs } }

/**
 * Returns a parser that does not consume input, even if it succeeds
 */
fun <T> Parser<T>.peek() = { input: Parseable ->
    val output = this(input)
    if (output == null) null
    else Pair(input, output.second)
}

/**
 * Returns a parser that consumes until another parser is successful
 * @param next The terminal parser. It will not consume input on success
 */
fun <S, T> Parser<S>.until(next: Parser<T>): Parser<Iterable<S>> =
    next.peek().mapTo(listOf<S>())
        .or(this.bind { until(next).map { xs -> listOf(it) + xs } })

/**
 * Given this parser, will return a parser that fails if the other parser succeeds
 * @param next The exceptional parser whose success causes this one's failure
 */
fun <S, T> Parser<S>.except(next: Parser<T>): Parser<S> = { input: Parseable ->
    val output = next(input)
    if (output != null) null
    else this(input)
}

/**
 * Defers constructing a parser until it's needed. Useful when parsers create circular references
 * @param fn The function to lazily create a parser
 */
fun <T> defer(fn: () -> Parser<T>) = { input: Parseable -> fn()(input) }

/**
 * Represents a single value, or nothing
 */
sealed class Option<T> {
    class None<T> : Option<T>()
    data class Some<T>(val value: T) : Option<T>()

    /**
     * @param default The value to use if this has no value
     * @return The given default if this has no value
     */
    fun valueOrDefault(default: T) = if (this is Some<T>) value else default
}

/**
 * Returns a parser that always succeeds and returns the optional value if input was consumed
 */
fun <T> Parser<T>.optional() = { input: Parseable ->
    val output = this(input)
    if (output == null) Pair(input, Option.None<T>())
    else Pair(output.first, Option.Some(output.second))
}

/**
 * Helper class to simulate substrings of an original string
 * @param value The original string
 * @param start The head of the substring
 */
data class Span(val value: String, val start: Int = 0) {
    val length = value.length - start
    fun substring(start: Int) = Span(value, this.start + start)
}
