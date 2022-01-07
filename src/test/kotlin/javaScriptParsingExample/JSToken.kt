package javaScriptParsingExample

sealed class JSToken {
    object JSNull : JSToken()
    data class JSBoolean(val value: Boolean) : JSToken()
    data class JSString(val value: String) : JSToken()
    data class JSNumber(val value: Double) : JSToken()
    data class JSArray(val value: Iterable<JSToken>) : JSToken()
    data class JSObject(val value: Iterable<Pair<String, JSToken>>) : JSToken()
    data class JSLambda(val args: Iterable<String>, val body: Iterable<JSToken>) : JSToken()
    data class JSAssignment(val name: String, val expression: JSToken) : JSToken()
    data class JSReturn(val expression: JSToken) : JSToken()
    enum class BinaryOperator { Add, Sub, Div, Mul, Exponent, Eq, Ne, Lt, Lte, Gt, Gte }
    enum class UnaryOperator { LogicalNegation, ArithmeticNegation, }
    sealed class Expr : JSToken() {
        data class Binary(val left: JSToken, val operator: BinaryOperator, val right: JSToken) : Expr()
        data class Unary(val expr: JSToken, val operator: UnaryOperator) : Expr()
    }
    data class VariableAccess(val namespace: Iterable<String>) : JSToken()
    data class FunctionCall(val namespace: Iterable<String>, val args: List<JSToken>) : JSToken()
    data class IfStatement(val condition: JSToken, val body: Iterable<JSToken>) : JSToken()
    data class IfElseStatement(val ifStatement: IfStatement, val elseBody: Iterable<JSToken>) : JSToken()
}