import org.junit.Test
import kotlin.test.assertEquals

class ExceptTest {
    @Test
    fun consumesUntilOtherParser() {
        assertEquals(Pair("a".parseable(1), 'a'), item.except(char('b'))("a".parseable()))
    }

    @Test
    fun consumesUntilOtherParser2() {
        assertEquals(null, item.except(char('b'))("b".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char('a').until(char('b'))("".parseable()))
    }
}