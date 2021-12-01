import org.junit.Test
import kotlin.test.assertEquals

class TokenTest {
    @Test
    fun returnsValueTrimmingWhitespace() {
        assertEquals(Pair("   a     ".parseable(9), 'a'), char('a').token()("   a     ".parseable()))
    }

    @Test
    fun returnsValue() {
        assertEquals(Pair("a".parseable(1), 'a'), char('a').token()("a".parseable()))
    }

    @Test
    fun doesNotSatisfyReturnsNull() {
        assertEquals(null, char('a').token()("   b     ".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char('a').token()("".parseable()))
    }
}
