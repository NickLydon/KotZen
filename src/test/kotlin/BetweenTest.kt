import org.junit.Test
import kotlin.test.assertEquals

class BetweenTest {
    @Test
    fun returnsIfInBookends() {
        assertEquals(Pair("", 'a'), char('a').between(char('['), char(']'))("[a]"))
    }

    @Test
    fun stopsIfNoMatchBetweenBookends() {
        assertEquals(null, char('a').between(char('['), char(']'))("[b]"))
    }

    @Test
    fun stopsIfLeftNotMatching() {
        assertEquals(null, char('a').between(char('['), char(']'))("(a]"))
    }

    @Test
    fun stopsIfRightNotMatching() {
        assertEquals(null, char('a').between(char('['), char(']'))("[a)"))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char('a').between(char('['), char(']'))(""))
    }
}