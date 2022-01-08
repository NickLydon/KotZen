import org.junit.Test
import kotlin.test.assertEquals

class SatTest {
    @Test
    fun satisfiesFunctionReturnsValue() {
        assertEquals(Pair("a".parseable(1), 'a'), sat { it == 'a' }("a".parseable()))
    }

    @Test
    fun doesNotSatisfyReturnsNull() {
        assertEquals(null, sat { it == 'b' }("a".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, sat { it == 'b' }("".parseable()))
    }
}

