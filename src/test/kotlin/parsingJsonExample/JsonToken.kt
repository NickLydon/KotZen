sealed class JsonToken {
    object JsonNull : JsonToken()
    data class JsonString(val value: String) : JsonToken()
    data class JsonBool(val value: Boolean) : JsonToken()
    data class JsonNumber(val value: Double) : JsonToken()
    data class JsonArray(val value: Iterable<JsonToken>) : JsonToken()
    data class JsonObject(val value: Iterable<Pair<String, JsonToken>>) : JsonToken()
}