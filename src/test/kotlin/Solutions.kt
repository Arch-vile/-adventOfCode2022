package aoc

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
    fun day5() {
        assertEquals("QGTHFZBHV", day5.part1())
        assertEquals("MGDMPSZTM", day5.part2())
    }

    @Test
    fun day6() {
        assertEquals(1531, day6.part1())
        assertEquals(2518, day6.part2())
    }

    @Test
    fun day7() {
        assertEquals(1453349, day7.part1())
        assertEquals(2948823, day7.part2())
    }

    @Test
    fun day8() {
        assertEquals(1538, day8.part1())
        assertEquals(496125, day8.part2())
    }

    @Test
    fun day9() {
        assertEquals(6367, day9.part1())
        assertEquals(2536, day9.part2())
    }

    @Test
    fun day10() {
        assertEquals(13520, day10.part1())
        assertEquals("###...##..###..#..#.###..####..##..###..\n#..#.#..#.#..#.#..#.#..#.#....#..#.#..#.\n#..#.#....#..#.####.###..###..#..#.###..\n###..#.##.###..#..#.#..#.#....####.#..#.\n#....#..#.#....#..#.#..#.#....#..#.#..#.\n#.....###.#....#..#.###..####.#..#.###..\n", day10.part2())
    }

    @Test
    fun day11() {
        assertEquals("113232", day11.part1())
        assertEquals("29703395016", day11.part2())
    }
    @Test
    fun day12() {
        assertEquals(437, day12.part1())
        assertEquals(430, day12.part2())
    }
    @Test
    fun day17() {
        assertEquals(3219, day17.part1())
//        assertEquals(430, day17.part2())
    }
    @Test
    fun day18() {
        assertEquals(4320, day18.part1())
//        assertEquals(1, dayWIP.part2())
    }

    @Test
    fun day21() {
        assertEquals(194501589693264L, day21.part1())
        assertEquals(3887609741189L, day21.part2())
    }

    @Test
    fun day_WIP() {
        assertEquals(1, dayWIP.part1())
        assertEquals(1, dayWIP.part2())
    }

}
