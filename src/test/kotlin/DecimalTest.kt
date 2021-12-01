import org.junit.Test
import kotlin.test.assertEquals

class DecimalTest {
    @Test
    fun parsesNegativeNumber() {
        assertEquals(Pair("", -123.0), decimal("-123"))
    }

    @Test
    fun parsesPositiveNumber() {
        assertEquals(Pair("", 123.0), decimal("123"))
    }

    @Test
    fun parsesNegativeDecimal() {
        assertEquals(Pair("", -123.6), decimal("-123.6"))
    }

    @Test
    fun parsesPositiveDecimal() {
        assertEquals(Pair("", 123.7), decimal("123.7"))
    }

    @Test
    fun doesNotSatisfyReturnsNull() {
        assertEquals(null, decimal("a"))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, decimal(""))
    }
}