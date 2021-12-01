import org.junit.Test
import kotlin.test.assertEquals

class ItemTest {
    @Test
    fun singleCharTest() {
        assertEquals(Pair("", 'a'), item("a"))
    }

    @Test
    fun doubleCharTest() {
        assertEquals(Pair("b", 'a'), item("ab"))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, item(""))
    }
}

