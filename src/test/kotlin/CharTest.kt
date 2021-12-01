import org.junit.Test
import kotlin.test.assertEquals

class CharTest {
    @Test
    fun satisfiesFunctionReturnsValue() {
        assertEquals(Pair("", 'a'), char('a')("a"))
    }

    @Test
    fun doesNotSatisfyReturnsNull() {
        assertEquals(null, char('b')("a"))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char('b')(""))
    }
}


