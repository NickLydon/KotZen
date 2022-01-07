import org.junit.Test
import kotlin.test.assertEquals

class AtLeastOneTest {
    @Test
    fun ifNotFoundReturnsNull() {
        assertEquals(null, sat { x -> x == 'a' }.atLeastOne()("b".parseable()))
    }

    @Test
    fun returnsOne() {
        assertEquals(Pair("ab".parseable(1), listOf('a')), sat { x -> x == 'a' }.atLeastOne()("ab".parseable()))
    }

    @Test
    fun returnsTwo() {
        assertEquals(Pair("aab".parseable(2), listOf('a', 'a')), sat { x -> x == 'a' }.atLeastOne()("aab".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char.atLeastOne()("".parseable()))
    }
}

