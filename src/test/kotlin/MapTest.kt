import org.junit.Test
import kotlin.test.assertEquals

class MapTest {
    @Test
    fun mapsAChar() {
        assertEquals(Pair("", 'A'), item.map { x -> x.uppercaseChar() }("a"))
    }

    @Test
    fun mapsAChar2() {
        assertEquals(Pair("b", 'A'), item.map { x -> x.uppercaseChar() }("ab"))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, item.map { x -> x.uppercaseChar() }(""))
    }
}

