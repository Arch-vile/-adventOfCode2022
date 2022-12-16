package aoc.aoc.utils

import aoc.utils.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CustomStringExtensionsTest {

    @Test
    fun findGroups() {
        assertEquals(
            listOf("3","4"),
        "kjasdf age 3 fla age 4".findGroups("""age (\d+)""".toRegex()))
    }

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


}