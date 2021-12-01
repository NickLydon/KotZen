import org.junit.Test
import kotlin.test.assertEquals

class UntilTest {
    @Test
    fun consumesUntilOtherParser() {
        assertEquals(Pair("b", listOf('a', 'a', 'a')), char('a').until(char('b'))("aaab"))
    }

    @Test
    fun returnsNullWhenOtherParserEmpty() {
        assertEquals(null, char('a').until(char('b'))("aaac"))
    }

    @Test
    fun returnsNullWhenOtherParserEmpty2() {
        assertEquals(null, char('a').until(char('b'))("aaa"))
    }

    @Test
    fun noMatchReturnsEmpty() {
        assertEquals(Pair("b", listOf()), char('a').until(char('b'))("b"))
    }

    @Test
    fun noMatchReturnsNull2() {
        assertEquals(null, char('a').until(char('b'))("c"))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char('a').until(char('b'))(""))
    }
}

