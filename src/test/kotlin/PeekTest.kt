import org.junit.Test
import kotlin.test.assertEquals

class PeekTest {
    @Test
    fun shouldNotConsumeInput() {
        assertEquals(Pair("a".parseable(), 'a'), char('a').peek()("a".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char('a').peek()("".parseable()))
    }
}