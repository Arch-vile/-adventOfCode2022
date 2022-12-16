import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Solutions {

    @Test
    fun testSum() {
        val expected = 42
        assertEquals(expected, 40 + 2)
    }

    @Test
    fun day1() {
        assertEquals(69501, day1.part1())
        assertEquals(202346, day1.part2())
    }

}
