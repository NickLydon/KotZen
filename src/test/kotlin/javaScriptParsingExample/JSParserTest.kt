package javaScriptParsingExample

import org.junit.Test
import kotlin.test.assertEquals

class JSParserTest {
    val fullExample =
        """
    const a = 1;
    const b = "hello";
    const c = true;
    const d = [1, true, "hello"];
    const e = { "key": "value" };
    const f = () => {
        return 1;
    };
    const g = (f() + 10)^2 / 2 - (-10);
    const factorial = (n) => {
        if (n <= 1) {
            return 1;
        } else {
            return n * factorial(n - 1); 
        }
    };
"""

    @Test
    fun testFullExample() {
        val result = JSParser().parse(fullExample)
        assertEquals(listOf(), result)
    }

    @Test
    fun testSingleAssignment() {
        val result = JSParser().parse(
            """
               const a = 1;
            """
        )
        assertEquals(listOf(JSToken.JSAssignment("a", JSToken.JSNumber(1.0))), result)
    }

    @Test
    fun testMultipleAssignment() {
        val result = JSParser().parse(
            """
                const a = 1;
                const b = 2;const c = 3;
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment("a", JSToken.JSNumber(1.0)),
                JSToken.JSAssignment("b", JSToken.JSNumber(2.0)),
                JSToken.JSAssignment("c", JSToken.JSNumber(3.0)),
            ),
            result)
    }

    @Test
    fun testArrayAssignment() {
        val result = JSParser().parse(
            """
                const a = [1, "one", true];
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "a",
                    JSToken.JSArray(
                        listOf(
                            JSToken.JSNumber(1.0),
                            JSToken.JSString("one"),
                            JSToken.JSBoolean(true)))),
            ),
            result)
    }

    @Test
    fun testEmptyArrayAssignment() {
        val result = JSParser().parse(
            """
                const a = [];
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "a",
                    JSToken.JSArray(listOf()))
            ),
            result)
    }

    @Test
    fun testObjectAssignment() {
        val result = JSParser().parse(
            """
                const a = { "X": true, "Y": {}, Z: [] };
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "a",
                    JSToken.JSObject(
                        listOf(
                            Pair("X", JSToken.JSBoolean(true)),
                            Pair("Y", JSToken.JSObject(listOf())),
                            Pair("Z", JSToken.JSArray(listOf())),
                        )
                    )
                )
            ),
            result)
    }

    @Test
    fun testEmptyObjectAssignment() {
        val result = JSParser().parse(
            """
                const a = { };
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "a",
                    JSToken.JSObject(listOf())
                )
            ),
            result)
    }

    @Test
    fun testLambda() {
        val result = JSParser().parse(
            """
const f = () => {
    const a = true;
    return 1;
};
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "f",
                    JSToken.JSLambda(
                        listOf(),
                        listOf(
                            JSToken.JSAssignment("a", JSToken.JSBoolean(true)),
                            JSToken.JSReturn(JSToken.JSNumber(1.0)),
                        )
                    )
                )
            ),
            result)
    }

    @Test
    fun testLambdaWithArguments() {
        val result = JSParser().parse(
            """
const f = (x, y) => {
    return 1;
};
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "f",
                    JSToken.JSLambda(
                        listOf("x", "y"),
                        listOf(JSToken.JSReturn(JSToken.JSNumber(1.0))))
                )
            ),
            result)
    }

    @Test
    fun testFunctionCall() {
        val result = JSParser().parse(
            """
const f = x.g(1, true);
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "f",
                    JSToken.FunctionCall(
                        listOf("x", "g"),
                        listOf(
                            JSToken.JSNumber(1.0),
                            JSToken.JSBoolean(true)
                        )
                    )
                )
            ),
            result)
    }

    @Test
    fun testFunctionCallWithoutArgs() {
        val result = JSParser().parse(
            """
const f = x.g();
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "f",
                    JSToken.FunctionCall(
                        listOf("x", "g"),
                        listOf()
                    )
                )
            ),
            result)
    }

    @Test
    fun testAdd() {
        val result = JSParser().parse(
            """
const f = 1 + 10;
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "f",
                    JSToken.Expr.Binary(JSToken.JSNumber(1.0), JSToken.BinaryOperator.Add, JSToken.JSNumber(10.0))
                )
            ),
            result)
    }

    @Test
    fun testSub() {
        val result = JSParser().parse(
            """
const f = 1 - 10;
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "f",
                    JSToken.Expr.Binary(JSToken.JSNumber(1.0), JSToken.BinaryOperator.Sub, JSToken.JSNumber(10.0))
                )
            ),
            result)
    }

    @Test
    fun testMul() {
        val result = JSParser().parse(
            """
const f = 1 * 10;
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "f",
                    JSToken.Expr.Binary(JSToken.JSNumber(1.0), JSToken.BinaryOperator.Mul, JSToken.JSNumber(10.0))
                )
            ),
            result)
    }

    @Test
    fun testDiv() {
        val result = JSParser().parse(
            """
const f = 1 / 10;
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "f",
                    JSToken.Expr.Binary(JSToken.JSNumber(1.0), JSToken.BinaryOperator.Div, JSToken.JSNumber(10.0))
                )
            ),
            result)
    }

    @Test
    fun testExponent() {
        val result = JSParser().parse(
            """
const f = 2^3;
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "f",
                    JSToken.Expr.Binary(JSToken.JSNumber(2.0), JSToken.BinaryOperator.Exponent, JSToken.JSNumber(3.0))
                )
            ),
            result)
    }

    @Test
    fun testArithmeticWithVariables() {
        val result = JSParser().parse(
            """
const f = x ^ y();
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "f",
                    JSToken.Expr.Binary(
                        JSToken.VariableAccess(listOf("x")),
                        JSToken.BinaryOperator.Exponent,
                        JSToken.FunctionCall(listOf("y"), listOf()))
                )
            ),
            result)
    }

    @Test
    fun testAssociativity() {
        val result = JSParser().parse(
            """
const f = 1 + (2 * 3) ^ 4 - 5;
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "f",
                    JSToken.Expr.Binary(
                        JSToken.JSNumber(1.0),
                        JSToken.BinaryOperator.Add,
                        JSToken.Expr.Binary(
                            JSToken.Expr.Binary(
                                JSToken.Expr.Binary(
                                    JSToken.JSNumber(2.0),
                                    JSToken.BinaryOperator.Mul,
                                    JSToken.JSNumber(3.0)
                                ),
                                JSToken.BinaryOperator.Exponent,
                                JSToken.JSNumber(4.0)
                            ),
                            JSToken.BinaryOperator.Sub,
                            JSToken.JSNumber(5.0)
                        )
                    )
                )
            ),
            result
        )
    }


    @Test
    fun testVariableAccess() {
        val result = JSParser().parse(
            """
const f = x.y;
            """
        )
        assertEquals(
            listOf(
                JSToken.JSAssignment(
                    "f",
                    JSToken.VariableAccess(listOf("x", "y"))
                )
            ),
            result)
    }

}