package javaScriptParsingExample

import Parser
import alpha
import between
import bind
import char
import decimal
import defer
import delimitedBy
import digit
import except
import first
import many
import map
import optional
import or
import parse
import pure
import skipLeft
import skipRight
import symbol
import text
import token
import whitespace

class JSParser {
    private val parens = Pair(char('(').token(), char(')').token())
    private val braces = Pair(char('{').token(), char('}').token())
    private val brackets = Pair(char('[').token(), char(']').token())
    private val reservedKeywords = listOf("null", "true", "false", "return", "if", "else")
        .map(::symbol)
        .first()
    private val nullP = symbol("null").map { JSToken.JSNull }.token()
    private val boolP = symbol("true").map { true }.or(symbol("false").map { false }).map { JSToken.JSBoolean(it) }.token()
    private val stringP =
        char('\\').bind { char }.or(char.except(char('"'))).many()
            .between(char('"'))
        .or(
        char('\\').bind { char }.or(char.except(char('\''))).many()
            .between(char('\''))
        )
        .text()
        .map { JSToken.JSString(it) }
        .token()
    private val numP = decimal.map { JSToken.JSNumber(it) }.token()
    private val literalP = listOf(nullP, boolP, stringP, numP).first()
    private val identifierP =
        alpha.or(char('_')).bind { alpha.or(char('_')).or(digit).many().text().map { xs -> it + xs } }.token()
            .except(reservedKeywords)
    private fun jsExpression() : Parser<JSToken> = listOf(lambdaP, unaryOrBinaryP(), literalP, arrayP, objectP, functionCallP, variableAccessP).first()
    private val assignmentP =
        identifierP.skipRight(char('=').token()).bind { left ->
            jsExpression().token().map { right -> JSToken.JSAssignment(left, right) }
        }.between(symbol("const").token(), char(';').token())

    private val variableAccessP = identifierP.delimitedBy(char('.')).map { JSToken.VariableAccess(it) }
    private val arrayP =
        defer(::jsExpression)
            .delimitedBy(char(',').token())
            .optional()
            .between(brackets)
            .map { JSToken.JSArray(it.valueOrDefault(listOf())) }
            .token()

    private val objectP =
        identifierP.map { JSToken.JSString(it) }.or(stringP).skipRight(char(':').token())
            .bind { key -> jsExpression().map { value -> Pair(key.value, value) } }
            .delimitedBy(char(',').token())
            .optional()
            .between(braces)
            .map { JSToken.JSObject(it.valueOrDefault(listOf())) }
            .token()

    private val returnP = defer(::jsExpression).map { JSToken.JSReturn(it) }.between(symbol("return").token(), char(';').token())

    private val lambdaP =
        defer {
            val argList = identifierP.delimitedBy(char(',').token()).optional().map { it.valueOrDefault(listOf()) }
                .between(parens)
            val body = jsTokenPs().between(braces)
            argList.skipRight(symbol("=>").token()).bind { args ->
                body.map { JSToken.JSLambda(args, it) }
            }
        }

    private val functionCallP = variableAccessP.bind { id ->
        jsExpression().delimitedBy(char(',').token()).optional().map { JSToken.FunctionCall(id.namespace, it.valueOrDefault(listOf())) }
            .between(parens)
    }

    private fun unaryOrBinaryP(): Parser<JSToken> {
        fun binaryOpParser(lowerPriority: Parser<JSToken>, operators: List<Parser<JSToken.BinaryOperator>>): Parser<JSToken> =
            lowerPriority.bind { f ->
                operators.map { it.token() }.first().bind { operator ->
                    binaryOpParser(lowerPriority, operators).map { t -> JSToken.Expr.Binary(f, operator, t) }
                }.or(pure(f))
            }
        fun unaryOpParser(operators: List<Parser<JSToken.UnaryOperator>>): Parser<JSToken> =
            operators.map { it.token() }.first().bind { operator ->
                unaryOrBinaryP().map { JSToken.Expr.Unary(it, operator) }
            }

        val parens = defer(::unaryOrBinaryP).between(parens)
        val factor = listOf(parens, numP, boolP, functionCallP, variableAccessP).first()
        val unary = unaryOpParser(
            listOf(
                char('!').map { JSToken.UnaryOperator.LogicalNegation },
                char('-').map { JSToken.UnaryOperator.ArithmeticNegation },
            )
        )
        val exp = binaryOpParser(
            unary.or(factor),
            listOf(
                char('^').map { JSToken.BinaryOperator.Exponent },
                symbol("==").map { JSToken.BinaryOperator.Eq },
                symbol("!=").map { JSToken.BinaryOperator.Ne },
                symbol(">=").map { JSToken.BinaryOperator.Gte },
                symbol("<=").map { JSToken.BinaryOperator.Lte },
                symbol(">").map { JSToken.BinaryOperator.Gt },
                symbol("<").map { JSToken.BinaryOperator.Lt },
            ),
        )
        val term = binaryOpParser(exp, listOf(char('*').map { JSToken.BinaryOperator.Mul }, char('/').map { JSToken.BinaryOperator.Div }))
        val expr = binaryOpParser(term, listOf(char('+').map { JSToken.BinaryOperator.Add }, char('-').map { JSToken.BinaryOperator.Sub }))
        return expr
    }

    private fun ifStatementP(): Parser<JSToken> =
        symbol("if").token().skipLeft(
            jsExpression().between(parens).bind { condition ->
                jsTokenPs().between(braces).map { body ->
                    JSToken.IfStatement(condition, body)
                }
            }
        ).bind { ifS ->
            symbol("else").token().skipLeft(
                jsTokenPs().between(braces).map { body ->
                    JSToken.IfElseStatement(ifS, body)
                }
            ).or(pure(ifS))
        }

    private fun jsTokenPs() = assignmentP.or(ifStatementP()).or(returnP).many().skipRight(whitespace.many())

    fun parse(input: String) = jsTokenPs().parse(input)
}
