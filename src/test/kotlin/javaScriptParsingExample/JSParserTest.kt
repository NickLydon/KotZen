package javaScriptParsingExample

class JSParserTest {
    val example =
"""
    const a = 1
    const b = "hello"
    const c = true
    const d = [1, true, "hello"]
    const e = { "key": "value" } 
    const f = () => {
        return 1
    }
    const g = (f() + 10)^2 / 2 - (-10)
    const factorial = (n) => {
        if (n <= 1) {
            return 1
        } else {
            return n * factorial(n - 1) 
        }
    }
"""
}