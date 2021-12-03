typealias ParserOutput<T> = Pair<Parseable, T>
typealias Parser<T> = (Parseable) -> ParserOutput<T>?
interface Parseable {
    fun advance(steps: Int) : Parseable
    fun remaining() : Int
    fun head() : Char
    fun isEmpty() = remaining() == 0
    fun unparsed() : String
}

data class StringParseable(private val string: String, private val index : Int = 0) : Parseable {
    override fun advance(steps: Int): Parseable = StringParseable(string, index + steps)
    override fun remaining(): Int = 0.coerceAtLeast(string.length - index)
    override fun head(): Char = string[index]
    override fun unparsed(): String = string.substring(index)
}

fun String.parseable(index : Int = 0) = StringParseable(this, index)

fun <T> Parser<T>.parse(input: String): T {
    val output = this(StringParseable(input)) ?: error("Invalid input")

    if (!output.first.isEmpty()) error("Unconsumed input: " + output.first.unparsed())

    return output.second
}

fun <S, T> Parser<S>.map(fn: (S) -> T): Parser<T> = { input: Parseable ->
    val output = this(input)
    if (output == null) null
    else Pair(output.first, fn(output.second))
}

val item = { input: Parseable ->
    if (input.isEmpty()) null
    else Pair(input.advance(1), input.head())
}

fun Parser<Iterable<Char>>.text() = this.map { it.joinToString("") }

fun sat(fn: (Char) -> Boolean) = { input: Parseable ->
    val output = item(input)
    if (output == null || !fn(output.second)) null
    else output
}

val isDigit = sat { c -> c.isDigit() }

val number = isDigit.atLeastOne().map { xs -> xs.joinToString("").toInt() }

val integer =
    char('-').bind {
        number.map { num -> -num }
    }.or(number)

val decimal =
    integer.skipRight(char('.')).bind { i -> number.map { d -> "$i.$d".toDouble() } }
        .or(integer.map { x -> x.toDouble() })

val whitespace = sat { c -> c.isWhitespace() }

fun <T> Parser<T>.token() =
    whitespace.many().bind { this }.bind { output -> whitespace.many().map { output } }

fun char(c: Char) = sat { c2 -> c == c2 }

val alpha = sat { c -> c.isLetter() }

fun <T> Parser<T>.or(alternative: Parser<T>) = { input: Parseable ->
    this(input) ?: alternative(input)
}

fun <T> pure(default: T) = { input: Parseable ->
    Pair(input, default)
}

fun <T> Parser<T>.many(): Parser<Iterable<T>> =
    this.bind { initial ->
        this.many().map { xs -> listOf(initial) + xs }
    }.or(pure(listOf()))

fun <T> Parser<T>.atLeastOne() =
    this.bind { initial ->
        this.many().map { xs -> listOf(initial) + xs }
    }

fun <T, S> Parser<T>.bind(fn: (T) -> Parser<S>) = { input: Parseable ->
    val output = this(input)
    if (output == null) null
    else fn(output.second)(output.first)
}

fun <T, S> Parser<T>.delimitedBy(delimiter: Parser<S>) =
    this.bind { initial ->
        delimiter.skipLeft(this).many().map { xs -> listOf(initial) + xs }
    }

fun <T, S, U> Parser<T>.between(left: Parser<S>, right: Parser<U>) =
    left.skipLeft(this.skipRight(right))

fun <T, S> Parser<T>.skipLeft(next: Parser<S>) = this.bind { next }
fun <T, S> Parser<T>.skipRight(next: Parser<S>) = this.bind { x -> next.map { x } }

fun symbol(value: String): Parser<String> =
    if (value.isEmpty()) error("Expected a non-empty symbol")
    else char(value[0]).bind { c ->
        if (value.length == 1) pure(c.toString())
        else symbol(value.substring(1)).map { xs -> c + xs }
    }

fun <T> Parser<T>.peek() = { input: Parseable ->
    val output = this(input)
    if (output == null) null
    else Pair(input, output.second)
}

fun <S, T> Parser<S>.until(next: Parser<T>): Parser<Iterable<S>> =
    next.peek().map { listOf<S>() }
        .or(this.bind { x -> until(next).map { xs -> listOf(x) + xs } })

fun <S, T> Parser<S>.except(next: Parser<T>): Parser<S> = { input: Parseable ->
    val output = next(input)
    if (output != null) null
    else this(input)
}

fun <T> defer(fn: () -> Parser<T>) = { input: Parseable -> fn()(input) }

sealed class Option<T> {
    class None<T> : Option<T>()
    data class Some<T>(val value: T) : Option<T>()

    fun valueOrDefault(default: T) = if (this is Some<T>) value else default
}

fun <T> Parser<T>.optional() = { input: Parseable ->
    val output = this(input)
    if (output == null) Pair(input, Option.None<T>())
    else Pair(output.first, Option.Some(output.second))
}