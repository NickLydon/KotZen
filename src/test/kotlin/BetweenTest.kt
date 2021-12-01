import org.junit.Test
import kotlin.test.assertEquals

class BetweenTest {
    @Test
    fun returnsIfInBookends() {
        assertEquals(Pair("[a]".parseable(3), 'a'), char('a').between(char('['), char(']'))("[a]".parseable()))
    }

    @Test
    fun stopsIfNoMatchBetweenBookends() {
        assertEquals(null, char('a').between(char('['), char(']'))("[b]".parseable()))
    }

    @Test
    fun stopsIfLeftNotMatching() {
        assertEquals(null, char('a').between(char('['), char(']'))("(a]".parseable()))
    }

    @Test
    fun stopsIfRightNotMatching() {
        assertEquals(null, char('a').between(char('['), char(']'))("[a)".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char('a').between(char('['), char(']'))("".parseable()))
    }
}