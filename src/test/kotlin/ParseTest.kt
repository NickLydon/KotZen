import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ParseTest {
    @Test
    fun throwsWhenDoesNotMatch() {
        try {
            char('a').parse("b")
            fail("Should have thrown")
        }
        catch (e: IllegalStateException) {
            assertEquals("Invalid input", e.message)
        }
    }

    @Test
    fun throwsWhenInputLeftOver() {
        try {
            char('a').parse("ab")
            fail("Should have thrown")
        }
        catch (e: IllegalStateException) {
            assertEquals("Unconsumed input: b", e.message)
        }
    }

    @Test
    fun returnsValue() {
        assertEquals('a', char('a').parse("a"))
    }
}