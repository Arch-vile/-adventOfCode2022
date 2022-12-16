package aoc.aoc.utils

import aoc.utils.findFirstInt
import aoc.utils.findSecondInt
import aoc.utils.findSections
import aoc.utils.secondAsInt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CustomStringExtensionsTest {

    @Test
    fun second_as_int() {
        assertEquals(2,"1 2".secondAsInt())
        assertEquals(2,"1,2".secondAsInt(","))
    }
    @Test
    fun find_int() {
        assertEquals(
            123,
            "123".findFirstInt()
        )
        assertEquals(
            123,
            "abc 123".findFirstInt()
        )
        assertEquals(
            456,
            "abc 123 afe 456 j".findSecondInt()
        )
    }

    @Test
    fun findSection() {
        assertEquals(
            listOf(
                listOf(3,3),
                listOf(3,3,4),
                listOf(3,4),
                listOf(3,4,5)
            ),
            listOf(1,2,3,3,4,5,6,7).findSections(2..3) { it -> it.first() == 3}

        )
    }

}
