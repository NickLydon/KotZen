package javaScriptParsingExample

import Parser
import alpha
import atLeastOne
import between
import bind
import char
import decimal
import defer
import delimitedBy
import except
import item
import many
import map
import optional
import or
import skipLeft
import skipRight
import symbol
import text
import token
import whitespace

class JSParser {
    private val newline = symbol("\r\n").or(symbol("\r"))
    private val nullP = symbol("null").map { JSToken.JSNull }.token()
    private val boolP = symbol("true").map { true }.or(symbol("false").map { false }).map { JSToken.JSBoolean(it) }.token()
    private val stringP =
        char('\\').bind { item.map { c -> c } }.or(item.except(char('"'))).many()
            .between(char('"'), char('"'))
        .or(
        char('\\').bind { item.map { c -> c } }.or(item.except(char('\''))).many()
            .between(char('\''), char('\''))
        )
        .text()
        .map { JSToken.JSString(it) }
        .token()
    private val numP = decimal.map { JSToken.JSNumber(it) }.token()
    private val literalP = nullP.or(boolP).or(stringP).or(numP)
    private val arrayP by lazy {
        jsTokenP()
            .delimitedBy(char(','))
            .optional()
            .between(char('['), char(']'))
            .map { JSToken.JSArray(it.valueOrDefault(listOf())) }
            .token()
    }

    private val objectP by lazy {
        stringP.skipRight(char(':').token())
            .bind { key -> jsTokenP().map { value -> Pair(key.value, value) } }
            .delimitedBy(char(','))
            .optional()
            .between(char('{'), char('}'))
            .map { JSToken.JSObject(it.valueOrDefault(listOf())) }
            .token()
    }
    private val identifierP = alpha.or(char('_')).bind { x -> alpha.or(char('_')).many().text().map { xs -> x + xs } }.token()
    private val lambdaP by lazy {
        val argList = identifierP.delimitedBy(char(',').token()).optional().map { it.valueOrDefault(listOf()) }
            .between(char('(').token(), char(')').token())
        val returnP = symbol("return").skipRight(whitespace.atLeastOne()).skipLeft(jsTokenP()).map { JSToken.JSReturn(it) }
        val body = assignmentP.delimitedBy(newline).optional()
            .bind { assignments ->
                returnP.map { assignments.valueOrDefault(listOf()) + listOf(it) }
            }
        argList.skipRight(symbol("=>").token()).bind { args ->
            body.map { JSToken.JSLambda(args, it) }
        }.between(char('{').token(), char('}').token())
    }
    private val functionCallP = identifierP.bind { id ->
        jsTokenP().delimitedBy(char(',').token()).map { JSToken.FunctionCall(id, it) }
            .between(char('(').token(), char(')').token())
    }

    private val assignmentP = identifierP.skipRight(char('=')).bind {
        left -> literalP.or(arrayP).or(objectP).token().map { right -> JSToken.JSAssignment(left, right) }
    }.token()

    private fun arithmeticP(): Parser<JSToken.Expr> =
        defer { arithmeticP() }.between(char('(').token(), char(')').token())
            .or(defer { arithmeticP() }.skipRight(char('^').token()).map { JSToken.Expr.Unary(it, JSToken.UnaryOperator.Exponent) })
            .or(defer { arithmeticP() }.bind { left ->
                char('*').token().map { JSToken.BinaryOperator.Mul }.or(
                char('/').token().map { JSToken.BinaryOperator.Div })
                .bind { operator -> arithmeticP().map { right -> JSToken.Expr.Binary(left, operator, right) } }
            })
            .or(defer { arithmeticP() }.bind { left ->
                char('+').token().map { JSToken.BinaryOperator.Add }.or(
                char('-').token().map { JSToken.BinaryOperator.Sub })
                .bind { operator -> arithmeticP().map { right -> JSToken.Expr.Binary(left, operator, right) } }
            })
            .or(numP.token().map{ JSToken.Expr.Num(it.value) })

    private fun jsTokenP() : Parser<JSToken> =
        objectP.or(arrayP).or(lambdaP).or(functionCallP).or(assignmentP).or(arithmeticP())
}

