import org.junit.Test
import kotlin.test.assertEquals

sealed class JsonToken {
    object JsonNull : JsonToken()
    data class JsonString(val value: String) : JsonToken()
    data class JsonBool(val value: Boolean) : JsonToken()
    data class JsonNumber(val value: Double) : JsonToken()
    data class JsonArray(val value: Iterable<JsonToken>) : JsonToken()
    data class JsonObject(val value: Iterable<Pair<String, JsonToken>>) : JsonToken()
}

class JsonParser {
    private val nullP = symbol("null").map { JsonToken.JsonNull }.token()
    private val boolP = symbol("true").map { true }.or(symbol("false").map { false }).map { JsonToken.JsonBool(it) }.token()
    private val stringP =
        char('\\').bind { item.map { c -> c } }.or(item.except(char('"'))).many()
            .between(char('"'), char('"'))
            .map { JsonToken.JsonString(it.joinToString("")) }
            .token()
    private val numP = decimal.map { JsonToken.JsonNumber(it) }.token()
    private val literalP = nullP.or(boolP).or(stringP).or(numP)
    private fun arrayP() : Parser<JsonToken> =
        defer { jsonTokenP() }
            .delimitedBy(char(','))
            .optional()
            .between(char('['), char(']'))
            .map {JsonToken.JsonArray(it.valueOrDefault(listOf())) }
            .token()
    private fun objectP() =
        stringP.skipRight(char(':').token())
            .bind { key -> jsonTokenP().map { value -> Pair(key.value, value) } }
            .delimitedBy(char(','))
            .optional()
            .between(char('{'), char('}'))
            .map { JsonToken.JsonObject(it.valueOrDefault(listOf())) }
            .token()
    private fun jsonTokenP() : Parser<JsonToken> =
        literalP
            .or(objectP())
            .or(arrayP())

    fun parse(input: String) = jsonTokenP().parse(input)
}

class Json {

    @Test
    fun tokenEquality() {
        assertEquals(
            JsonToken.JsonObject(listOf(Pair("prop", JsonToken.JsonBool(true)), Pair("prop2", JsonToken.JsonArray(listOf(JsonToken.JsonString("hello"))) ))),
            JsonToken.JsonObject(listOf(Pair("prop", JsonToken.JsonBool(true)), Pair("prop2", JsonToken.JsonArray(listOf(JsonToken.JsonString("hello"))) ))))
    }

    @Test
    fun endToEnd() {
        val json =
            """
                {
                    "StringName": "String Value",
                    "BoolName": true,
                    "ArrayName": [1, "hello", false, {}],
                    "ObjectName": {
                        "NestedProperty": []
                    }
                }
            """
        val expected = JsonToken.JsonObject(
            listOf(
                Pair("StringName", JsonToken.JsonString("String Value")),
                Pair("BoolName", JsonToken.JsonBool(true)),
                Pair(
                    "ArrayName", JsonToken.JsonArray(
                        listOf(
                            JsonToken.JsonNumber(1.0),
                            JsonToken.JsonString("hello"),
                            JsonToken.JsonBool(false),
                            JsonToken.JsonObject(listOf())
                        )
                    )
                ),
                Pair(
                    "ObjectName", JsonToken.JsonObject(
                        listOf(
                            Pair("NestedProperty", JsonToken.JsonArray(listOf()))
                        )
                    )
                )
            )
        )

        val output = JsonParser().parse(json)

        assertEquals(expected, output)
    }

    @Test
    fun stringP() {
        assertEquals("hello!", when (val x = JsonParser().parse(""" "hello!" """)) {
            is JsonToken.JsonString -> x.value
            else -> null
        })
    }

    @Test
    fun stringP2() {
        assertEquals("\"\\hello!", when (val x = JsonParser().parse(""" "\"\\hello!" """)) {
            is JsonToken.JsonString -> x.value
            else -> null
        })
    }

    @Test
    fun boolP() {
        assertEquals(true, when (val x = JsonParser().parse(" true ")) {
            is JsonToken.JsonBool -> x.value
            else -> null
        })
    }

    @Test
    fun nullP() {
        assertEquals(JsonToken.JsonNull, JsonParser().parse(" null "))
    }

    @Test
    fun numP() {
        assertEquals(3.14, when (val x = JsonParser().parse(" 3.14 ")) {
            is JsonToken.JsonNumber -> x.value
            else -> null
        })
    }

    @Test
    fun arrayP() {
        val expected = listOf(
            JsonToken.JsonNumber(1.0),
            JsonToken.JsonString("hello"),
            JsonToken.JsonBool(true),
            JsonToken.JsonNull
        )
        val actual = when (val x = JsonParser().parse(" [ 1, \"hello\", true, null ] ")) {
            is JsonToken.JsonArray -> x.value
            else -> null
        }
        assertEquals(expected, actual)
    }

    @Test
    fun emptyNestedArray() {
        assertEquals(listOf(JsonToken.JsonArray(listOf())), when (val x = JsonParser().parse("[[]]")) {
            is JsonToken.JsonArray -> x.value
            else -> null
        })
    }

    @Test
    fun emptyArray() {
        assertEquals(listOf(), when (val x = JsonParser().parse("[]")) {
            is JsonToken.JsonArray -> x.value
            else -> null
        })
    }

    @Test
    fun emptyObject() {
        assertEquals(listOf(), when (val x = JsonParser().parse("{}")) {
            is JsonToken.JsonObject -> x.value
            else -> null
        })
    }

    @Test
    fun emptyNestedObject() {
        assertEquals(listOf(Pair("p", JsonToken.JsonObject(listOf()))), when (val x = JsonParser().parse("{ \"p\": {} }")) {
            is JsonToken.JsonObject -> x.value
            else -> null
        })
    }
}
