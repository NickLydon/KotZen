import org.junit.Test
import kotlin.test.assertEquals

class OrTest {
    @Test
    fun orReturnsFirstSuccess() {
        assertEquals(Pair("ab".parseable(1), 'a'), sat { it == 'a' }.or(sat { it == 'b' })("ab".parseable()))
    }

    @Test
    fun orReturnsFirstSuccess2() {
        assertEquals(Pair("ba".parseable(1), 'b'), sat { it == 'a' }.or(sat { it == 'b' })("ba".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, char.or(char)("".parseable()))
    }
}

