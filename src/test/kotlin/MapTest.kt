import org.junit.Test
import kotlin.test.assertEquals

class MapTest {
    @Test
    fun mapsAChar() {
        assertEquals(Pair("a".parseable(1), 'A'), char.map { it.uppercaseChar() }("a".parseable()))
    }

    @Test
    fun mapsAChar2() {
        assertEquals(Pair("ab".parseable(1), 'A'), char.map { it.uppercaseChar() }("ab".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char.map { it.uppercaseChar() }("".parseable()))
    }
}

