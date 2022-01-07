# KotZen

## A parser combinator library in Kotlin

Inspired by [Sprache](https://github.com/sprache/Sprache) and reading Graham Hutton's [Programming in Haskell](https://www.cs.nott.ac.uk/~pszgmh/pih.html).

Here's an example of a [json parser](https://github.com/NickLydon/KotZen/blob/main/src/test/kotlin/parsingJsonExample/JsonParser.kt):
```kotlin
package parsingJsonExample

import JsonToken
import Parser
import between
import bind
import char
import decimal
import defer
import delimitedBy
import except
import many
import map
import optional
import or
import parse
import skipRight
import symbol
import text
import token

class JsonParser {
    private val nullP = symbol("null").map { JsonToken.JsonNull }.token()
    private val boolP = symbol("true").map { true }.or(symbol("false").map { false }).map { JsonToken.JsonBool(it) }.token()
    private val stringP =
        char('\\').bind { char }.or(char.except(char('"'))).many()
            .between(char('"'))
            .text()
            .map { JsonToken.JsonString(it) }
            .token()
    private val numP = decimal.map { JsonToken.JsonNumber(it) }.token()
    private val literalP = nullP.or(boolP).or(stringP).or(numP)
    private val arrayP =
        defer(::jsonTokenP)
            .delimitedBy(char(','))
            .optional()
            .between(char('['), char(']'))
            .map { JsonToken.JsonArray(it.valueOrDefault(listOf())) }
            .token()
    private val objectP =
        stringP.skipRight(char(':').token())
            .bind { key -> jsonTokenP().map { value -> Pair(key.value, value) } }
            .delimitedBy(char(',').token())
            .optional()
            .between(char('{').token(), char('}').token())
            .map { JsonToken.JsonObject(it.valueOrDefault(listOf())) }
            .token()
    private fun jsonTokenP() : Parser<JsonToken> = literalP.or(objectP).or(arrayP)

    fun parse(input: String) = jsonTokenP().parse(input)
}
```
There's also a parser for a language which is almost a [subset of javascript](https://github.com/NickLydon/KotZen/blob/main/src/test/kotlin/javaScriptParsingExample/JSParser.kt).
