import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class SymbolTest {
    @Test
    fun ifNotFoundReturnsNull() {
        assertEquals(null, symbol("ab")("ac".parseable()))
    }

    @Test
    fun returnsStringSymbol() {
        assertEquals(Pair("abcd".parseable(3), "abc"), symbol("abc")("abcd".parseable()))
    }

    @Test
    fun returnsStringSymbolAtEnd() {
        assertEquals(Pair("abc".parseable(3), "abc"), symbol("abc")("abc".parseable()))
    }

    @Test
    fun emptyExpectedSymbolThrows() {
        try {
            symbol("")("abc".parseable())
            fail("Should have thrown")
        }
        catch (e: IllegalStateException) {
            assertEquals("Expected a non-empty symbol", e.message)
        }
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, symbol("a")("".parseable()))
    }
}