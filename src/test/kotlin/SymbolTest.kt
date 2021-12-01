import org.junit.Test
import kotlin.test.assertEquals

class SymbolTest {
    @Test
    fun ifNotFoundReturnsNull() {
        assertEquals(null, symbol("ab")("ac"))
    }

    @Test
    fun returnsStringSymbol() {
        assertEquals(Pair("d", "abc"), symbol("abc")("abcd"))
    }

    @Test
    fun returnsStringSymbolAtEnd() {
        assertEquals(Pair("", "abc"), symbol("abc")("abc"))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, symbol("a")(""))
    }
}