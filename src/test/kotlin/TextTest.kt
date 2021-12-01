import org.junit.Test
import kotlin.test.assertEquals

class TextTest {
    @Test
    fun satisfiesFunctionReturnsValue() {
        assertEquals(Pair("", "aaa"), char('a').many().text()("aaa"))
    }

    @Test
    fun emptyReturnsEmpty() {
        assertEquals(Pair("", ""), item.many().text()(""))
    }
}