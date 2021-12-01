import org.junit.Test
import kotlin.test.assertEquals

class UntilTest {
    @Test
    fun consumesUntilOtherParser() {
        assertEquals(Pair("aaab".parseable(3), listOf('a', 'a', 'a')), char('a').until(char('b'))("aaab".parseable()))
    }

    @Test
    fun returnsNullWhenOtherParserEmpty() {
        assertEquals(null, char('a').until(char('b'))("aaac".parseable()))
    }

    @Test
    fun returnsNullWhenOtherParserEmpty2() {
        assertEquals(null, char('a').until(char('b'))("aaa".parseable()))
    }

    @Test
    fun noMatchReturnsEmpty() {
        assertEquals(Pair("b".parseable(0), listOf()), char('a').until(char('b'))("b".parseable()))
    }

    @Test
    fun noMatchReturnsNull2() {
        assertEquals(null, char('a').until(char('b'))("c".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char('a').until(char('b'))("".parseable()))
    }
}

