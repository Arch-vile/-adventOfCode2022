package aoc.utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CustomListExtensionsTest {

    @Test
    fun combinations() {
        Assertions.assertEquals(
            setOf(
                listOf(1),
                listOf(2),
                listOf(3),
                listOf(1, 2),
                listOf(1, 3),
                listOf(2, 3),
                listOf(1, 2, 3),
            ),
            listOf(1, 2, 3).combinations(),
        )
    }

    @Test
    fun combinations_with_duplicates() {
        Assertions.assertEquals(
            setOf(
                listOf(1),
                listOf(2),
                listOf(1, 2),
                listOf(2, 2),
                listOf(1, 2, 2),
            ),
            listOf(1, 2, 2).combinations(),
        )
    }


    @Test
    fun permutations() {
        Assertions.assertEquals(
            setOf(
                listOf(3, 2, 1),
                listOf(2, 3, 1),
                listOf(3, 1, 2),
                listOf(1, 3, 2),
                listOf(2, 1, 3),
                listOf(1, 2, 3)
            ),
            permutations(setOf(1, 2, 3))
        )
    }

    @Test
    fun intersect() {
        assertEquals(
            intersect(listOf(listOf(1,2))),
            listOf(1,2)
        )
        assertEquals(
            intersect(listOf(listOf(1,2),listOf(1,2))),
            listOf(1,2)
        )
        assertEquals(
            intersect(listOf(listOf(1,2),listOf(0,1,3,2))),
            listOf(1,2)
        )
        assertEquals(
            intersect(listOf(listOf(),listOf(0,1,3,2))),
            listOf()
        )
    }

    @Test
    fun findSection() {
        Assertions.assertEquals(
            listOf(
                listOf(3, 3),
                listOf(3, 3, 4),
                listOf(3, 4),
                listOf(3, 4, 5)
            ),
            listOf(1, 2, 3, 3, 4, 5, 6, 7).findSections(2..3) { it.section.first() == 3  }

        )

        Assertions.assertEquals(
            listOf(
                listOf(3, 4),
                listOf(3, 4, 5)
            ),
            listOf(1, 2, 3, 3, 4, 5, 6, 7).findSections(2..3) { it.section.first() == 3 && it.before.last() == 3 }

        )
    }
}
