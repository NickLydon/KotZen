import org.junit.Test
import kotlin.test.assertEquals

class DecimalTest {
    @Test
    fun parsesNegativeNumber() {
        assertEquals(Pair("-123".parseable(4), -123.0), decimal("-123".parseable()))
    }

    @Test
    fun parsesPositiveNumber() {
        assertEquals(Pair("123".parseable(3), 123.0), decimal("123".parseable()))
    }

    @Test
    fun parsesNegativeDecimal() {
        assertEquals(Pair("-123.6".parseable(6), -123.6), decimal("-123.6".parseable()))
    }

    @Test
    fun parsesPositiveDecimal() {
        assertEquals(Pair("123.7".parseable(5), 123.7), decimal("123.7".parseable()))
    }

    @Test
    fun doesNotSatisfyReturnsNull() {
        assertEquals(null, decimal("a".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, decimal("".parseable()))
    }
}