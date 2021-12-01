import org.junit.Test
import kotlin.test.assertEquals

class ItemTest {
    @Test
    fun singleCharTest() {
        assertEquals(Pair("a".parseable(1), 'a'), item("a".parseable()))
    }

    @Test
    fun doubleCharTest() {
        assertEquals(Pair("ab".parseable(1), 'a'), item("ab".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, item("".parseable()))
    }
}

