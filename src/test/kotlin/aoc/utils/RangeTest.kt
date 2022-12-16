package aoc.utils

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

internal class RangeTest {

    @Test
    fun closed_range() {
        assertTrue( closedRange(1,2).contains(1))
        assertTrue( closedRange(1,2).contains(2))
        assertFalse( closedRange(1,2).contains(3))
    }

    @Test
    fun contains_fully() {
        assertTrue(closedRange(1,1).containsFully(closedRange(1,1)))
        assertTrue(closedRange(1,2).containsFully(closedRange(1,2)))
        assertTrue(closedRange(1,2).containsFully(closedRange(1,1)))
        assertTrue(closedRange(1,3).containsFully(closedRange(1,2)))
        assertTrue(closedRange(1,3).containsFully(closedRange(2,2)))
        assertTrue(closedRange(1,3).containsFully(closedRange(2,3)))
        assertTrue(closedRange(1,10).containsFully(closedRange(2,4)))
    }
}
