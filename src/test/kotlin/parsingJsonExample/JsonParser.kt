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
import mapTo
import optional
import or
import parse
import skipRight
import symbol
import text
import token

class JsonParser {
    private val nullP = symbol("null").mapTo(JsonToken.JsonNull).token()
    private val boolP = symbol("true").mapTo(true).or(symbol("false").mapTo(false)).map { JsonToken.JsonBool(it) }.token()
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