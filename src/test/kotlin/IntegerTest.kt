import org.junit.Test
import kotlin.test.assertEquals

class IntegerTest {
    @Test
    fun parsesNegativeNumber() {
        assertEquals(Pair("", -123), integer("-123"))
    }

    @Test
    fun parsesPositiveNumber() {
        assertEquals(Pair("", 123), integer("123"))
    }

    @Test
    fun doesNotSatisfyReturnsNull() {
        assertEquals(null, integer("a"))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, integer(""))
    }
}
