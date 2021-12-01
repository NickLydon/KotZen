import org.junit.Test
import kotlin.test.assertEquals

class IntegerTest {
    @Test
    fun parsesNegativeNumber() {
        assertEquals(Pair("-123".parseable(4), -123), integer("-123".parseable()))
    }

    @Test
    fun parsesPositiveNumber() {
        assertEquals(Pair("123".parseable(3), 123), integer("123".parseable()))
    }

    @Test
    fun doesNotSatisfyReturnsNull() {
        assertEquals(null, integer("a".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, integer("".parseable()))
    }
}
