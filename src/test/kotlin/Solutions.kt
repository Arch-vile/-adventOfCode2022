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

    @Test
    fun day2() {
        assertEquals(13682, day2.part1())
        assertEquals(12881, day2.part2())
    }

    @Test
    fun day3() {
        assertEquals(8153, day3.part1())
        assertEquals(2342, day3.part2())
    }

    @Test
    fun day4() {
        assertEquals(556, day4.part1())
        assertEquals(876, day4.part2())
    }

    @Test
    fun day_WIP() {
        assertEquals(556, dayWIP.part1())
        assertEquals(876, dayWIP.part2())
    }

}
