import org.junit.Test
import kotlin.test.assertEquals

class ManyTest {
    @Test
    fun ifNotFoundReturnsEmpty() {
        assertEquals(Pair("b", listOf()), sat { x -> x == 'a' }.many()("b"))
    }

    @Test
    fun returnsOne() {
        assertEquals(Pair("b", listOf('a')), sat { x -> x == 'a' }.many()("ab"))
    }

    @Test
    fun returnsTwo() {
        assertEquals(Pair("b", listOf('a', 'a')), sat { x -> x == 'a' }.many()("aab"))
    }

    @Test
    fun emptyReturnsEmpty() {
        assertEquals(Pair("", listOf()), item.many()(""))
    }
}

