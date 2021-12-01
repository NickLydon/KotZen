import org.junit.Test
import kotlin.test.assertEquals

class CharTest {
    @Test
    fun satisfiesFunctionReturnsValue() {
        assertEquals(Pair("a".parseable(1), 'a'), char('a')("a".parseable()))
    }

    @Test
    fun doesNotSatisfyReturnsNull() {
        assertEquals(null, char('b')("a".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char('b')("".parseable()))
    }
}


