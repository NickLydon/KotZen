import org.junit.Test
import kotlin.test.assertEquals

class AtLeastOneTest {
    @Test
    fun ifNotFoundReturnsNull() {
        assertEquals(null, sat { x -> x == 'a' }.atLeastOne()("b"))
    }

    @Test
    fun returnsOne() {
        assertEquals(Pair("b", listOf('a')), sat { x -> x == 'a' }.atLeastOne()("ab"))
    }

    @Test
    fun returnsTwo() {
        assertEquals(Pair("b", listOf('a', 'a')), sat { x -> x == 'a' }.atLeastOne()("aab"))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, item.atLeastOne()(""))
    }
}

