import org.junit.Test
import kotlin.test.assertEquals

class SatTest {
    @Test
    fun satisfiesFunctionReturnsValue() {
        assertEquals(Pair("a".parseable(1), 'a'), sat { x -> x == 'a' }("a".parseable()))
    }

    @Test
    fun doesNotSatisfyReturnsNull() {
        assertEquals(null, sat { x -> x == 'b' }("a".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, sat { x -> x == 'b' }("".parseable()))
    }
}

