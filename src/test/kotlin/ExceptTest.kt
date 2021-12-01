import org.junit.Test
import kotlin.test.assertEquals

class ExceptTest {
    @Test
    fun consumesUntilOtherParser() {
        assertEquals(Pair("", 'a'), item.except(char('b'))("a"))
    }

    @Test
    fun consumesUntilOtherParser2() {
        assertEquals(null, item.except(char('b'))("b"))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char('a').until(char('b'))(""))
    }
}