import org.junit.Test
import kotlin.test.assertEquals

class TextTest {
    @Test
    fun satisfiesFunctionReturnsValue() {
        assertEquals(Pair("aaa".parseable(3), "aaa"), char('a').many().text()("aaa".parseable()))
    }

    @Test
    fun emptyReturnsEmpty() {
        assertEquals(Pair("".parseable(), ""), char.many().text()("".parseable()))
    }
}