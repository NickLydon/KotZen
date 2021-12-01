import org.junit.Test
import kotlin.test.assertEquals

class OrTest {
    @Test
    fun orReturnsFirstSuccess() {
        assertEquals(Pair("b", 'a'), sat { x -> x == 'a' }.or(sat { x -> x == 'b' })("ab"))
    }

    @Test
    fun orReturnsFirstSuccess2() {
        assertEquals(Pair("a", 'b'), sat { x -> x == 'a' }.or(sat { x -> x == 'b' })("ba"))
    }

    @Test
    fun emptyReturnsNull() {
        assertEquals(null, item.or(item)(""))
    }
}

