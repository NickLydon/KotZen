import org.junit.Test
import kotlin.test.assertEquals

class DelimitedByTest {
    @Test
    fun returnsIfNoDelimiterPresent() {
        assertEquals(Pair("aa".parseable(1), listOf('a')), char('a').delimitedBy(char(','))("aa".parseable()))
    }

    @Test
    fun stripsDelimiter() {
        assertEquals(Pair("a,a,a".parseable(5), listOf('a', 'a', 'a')), char('a').delimitedBy(char(','))("a,a,a".parseable()))
    }

    @Test
    fun stopsIfNoMatchBetweenDelimiters() {
        assertEquals(Pair("a,,a".parseable(1), listOf('a')), char('a').delimitedBy(char(','))("a,,a".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char('a').delimitedBy(char(','))("".parseable()))
    }
}

