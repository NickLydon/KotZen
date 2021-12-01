import org.junit.Test
import kotlin.test.assertEquals

class TokenTest {
    @Test
    fun returnsValueTrimmingWhitespace() {
        assertEquals(Pair("", 'a'), char('a').token()("   a     "))
    }

    @Test
    fun returnsValue() {
        assertEquals(Pair("", 'a'), char('a').token()("a"))
    }

    @Test
    fun doesNotSatisfyReturnsNull() {
        assertEquals(null, char('a').token()("   b     "))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char('a').token()(""))
    }
}
