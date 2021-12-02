import parsingJsonExample.JsonParser
import org.junit.Test
import kotlin.test.assertEquals

class JsonParserTest {

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
                    "StringName": "String \"Value\"",
                    "BoolName": true,
                    "ArrayName": [1, "hello", false, {}],
                    "ObjectName": {
                        "NestedProperty": []
                    }                    
                }
            """
        val expected = JsonToken.JsonObject(
            listOf(
                Pair("StringName", JsonToken.JsonString("String \"Value\"")),
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
        assertEquals(" \" \\ hello! ", when (val x = JsonParser().parse(""" " \" \\ hello! " """)) {
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
