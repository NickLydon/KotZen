package javaScriptParsingExample

sealed class JSToken {
    object JSNull : JSToken()
    data class JSBoolean(val value: Boolean) : JSToken()
    data class JSString(val value: String) : JSToken()
    data class JSNumber(val value: Double) : JSToken()
    data class JSArray(val value: List<JSToken>) : JSToken()
    data class JSObject(val value: List<Pair<String, JSToken>>) : JSToken()
    data class JSLambda(val args: List<String>, val body: List<JSToken>) : JSToken()
    data class JSAssignment(val name: String, val expression: JSToken) : JSToken()
    data class JSReturn(val expression: JSToken) : JSToken()
    enum class BinaryOperator { Add, Sub, Div, Mul, Exponent }
    enum class UnaryOperator { Negation }
    sealed class Expr : JSToken() {
        data class Binary(val left: JSToken, val operator: BinaryOperator, val right: JSToken) : Expr()
        data class Unary(val expr: JSToken, val operator: UnaryOperator) : Expr()
    }
    data class VariableAccess(val value: String) : JSToken()
    data class FunctionCall(val identifier: String, val args: List<JSToken>) : JSToken()
}