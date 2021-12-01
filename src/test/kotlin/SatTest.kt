import org.junit.Test
import kotlin.test.assertEquals

class SatTest {
    @Test
    fun satisfiesFunctionReturnsValue() {
        assertEquals(Pair("", 'a'), sat { x -> x == 'a' }("a"))
    }

    @Test
    fun doesNotSatisfyReturnsNull() {
        assertEquals(null, sat { x -> x == 'b' }("a"))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, sat { x -> x == 'b' }(""))
    }
}

