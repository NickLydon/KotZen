import org.junit.Test
import kotlin.test.assertEquals

class ItemTest {
    @Test
    fun singleCharTest() {
        assertEquals(Pair("a".parseable(1), 'a'), char("a".parseable()))
    }

    @Test
    fun doubleCharTest() {
        assertEquals(Pair("ab".parseable(1), 'a'), char("ab".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char("".parseable()))
    }
}

