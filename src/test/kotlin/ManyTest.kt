import org.junit.Test
import kotlin.test.assertEquals

class ManyTest {
    @Test
    fun ifNotFoundReturnsEmpty() {
        assertEquals(Pair("b".parseable(), listOf()), sat { x -> x == 'a' }.many()("b".parseable()))
    }

    @Test
    fun returnsOne() {
        assertEquals(Pair("ab".parseable(1), listOf('a')), sat { x -> x == 'a' }.many()("ab".parseable()))
    }

    @Test
    fun returnsTwo() {
        assertEquals(Pair("aab".parseable(2), listOf('a', 'a')), sat { x -> x == 'a' }.many()("aab".parseable()))
    }

    @Test
    fun emptyReturnsEmpty() {
        assertEquals(Pair("".parseable(), listOf()), item.many()("".parseable()))
    }
}

