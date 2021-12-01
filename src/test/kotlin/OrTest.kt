import org.junit.Test
import kotlin.test.assertEquals

class OrTest {
    @Test
    fun orReturnsFirstSuccess() {
        assertEquals(Pair("ab".parseable(1), 'a'), sat { x -> x == 'a' }.or(sat { x -> x == 'b' })("ab".parseable()))
    }

    @Test
    fun orReturnsFirstSuccess2() {
        assertEquals(Pair("ba".parseable(1), 'b'), sat { x -> x == 'a' }.or(sat { x -> x == 'b' })("ba".parseable()))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, item.or(item)("".parseable()))
    }
}

