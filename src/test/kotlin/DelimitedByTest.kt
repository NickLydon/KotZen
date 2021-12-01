import org.junit.Test
import kotlin.test.assertEquals

class DelimitedByTest {
    @Test
    fun returnsIfNoDelimiterPresent() {
        assertEquals(Pair("a", listOf('a')), char('a').delimitedBy(char(','))("aa"))
    }

    @Test
    fun stripsDelimiter() {
        assertEquals(Pair("", listOf('a', 'a', 'a')), char('a').delimitedBy(char(','))("a,a,a"))
    }

    @Test
    fun stopsIfNoMatchBetweenDelimiters() {
        assertEquals(Pair(",,a", listOf('a')), char('a').delimitedBy(char(','))("a,,a"))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char('a').delimitedBy(char(','))(""))
    }
}

