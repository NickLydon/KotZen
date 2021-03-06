import org.junit.Test
import kotlin.test.assertEquals

class AtLeastOneTest {
    @Test
    fun ifNotFoundReturnsNull() {
        assertEquals(null, sat { it == 'a' }.atLeastOne()("b".parseable()))
    }

    @Test
    fun returnsOne() {
        assertEquals(Pair("ab".parseable(1), listOf('a')), sat { it == 'a' }.atLeastOne()("ab".parseable()))
    }

    @Test
    fun returnsTwo() {
        assertEquals(Pair("aab".parseable(2), listOf('a', 'a')), sat { it == 'a' }.atLeastOne()("aab".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char.atLeastOne()("".parseable()))
    }
}

