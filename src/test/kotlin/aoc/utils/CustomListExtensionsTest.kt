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
            intersect(listOf(listOf(1, 2))),
            listOf(1, 2)
        )
        assertEquals(
            intersect(listOf(listOf(1, 2), listOf(1, 2))),
            listOf(1, 2)
        )
        assertEquals(
            intersect(listOf(listOf(1, 2), listOf(0, 1, 3, 2))),
            listOf(1, 2)
        )
        assertEquals(
            intersect(listOf(listOf(), listOf(0, 1, 3, 2))),
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
            listOf(1, 2, 3, 3, 4, 5, 6, 7).findSections(2..3) { it.section.first() == 3 }

        )

        Assertions.assertEquals(
            listOf(
                listOf(3, 4),
                listOf(3, 4, 5)
            ),
            listOf(1, 2, 3, 3, 4, 5, 6, 7).findSections(2..3) { it.section.first() == 3 && it.before.last() == 3 }

        )
    }

    @Test
    fun findIndexes() {
        assertEquals(
            listOf(),
            listOf(1, 2).indexesOf { it == 10 }
        )
        assertEquals(
            listOf(),
            listOf(1).indexesOf { it == 10 }
        )
        assertEquals(
            listOf(),
            listOf<Int>().indexesOf { it == 10 }
        )
        assertEquals(
            listOf(1),
            listOf(1, 2, 3, 4, 5).indexesOf { it == 2 }
        )
        assertEquals(
            listOf(0, 4),
            listOf(1, 2, 3, 4, 5).indexesOf { it == 1 || it == 5 }
        )
        assertEquals(
            listOf(0, 1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5).indexesOf { it < 10 }
        )

    }

    @Test
    fun breakBefore() {
        assertEquals(
            listOf(listOf(1, 2), listOf(3, 4)),
            listOf(1, 2, 3, 4).breakBefore { it == 3 }
        )
        assertEquals(
            listOf(listOf(1, 2), listOf(3, 4), listOf(3)),
            listOf(1, 2, 3, 4, 3).breakBefore { it == 3 }
        )
        assertEquals(
            listOf(),
            listOf<Int>().breakBefore { it == 4 }
        )
        assertEquals(
            listOf(listOf(), listOf(4)),
            listOf(4).breakBefore { it == 4 }
        )
        assertEquals(
            listOf(listOf(), listOf(4), listOf(4)),
            listOf(4, 4).breakBefore { it == 4 }
        )
        assertEquals(
            listOf(listOf(), listOf(4), listOf(5), listOf(6)),
            listOf(4, 5, 6).breakBefore { it >= 4 }
        )
        assertEquals(
            listOf(listOf(1, 2, 3)),
            listOf(1, 2, 3).breakBefore { it == 4 }
        )
        assertEquals(
            listOf(listOf(), listOf(1, 2, 3)),
            listOf(1, 2, 3).breakBefore { it == 1 }
        )
    }

    @Test
    fun breakAfter() {
        assertEquals(
            listOf(listOf(1, 2, 3), listOf(4)),
            listOf(1, 2, 3, 4).breakAfter { it == 3 }
        )
        assertEquals(
            listOf(listOf(1, 2, 3), listOf(4, 3), listOf()),
            listOf(1, 2, 3, 4, 3).breakAfter { it == 3 }
        )
        assertEquals(
            listOf(),
            listOf<Int>().breakAfter { it == 4 }
        )
        assertEquals(
            listOf(listOf(4), listOf()),
            listOf(4).breakAfter { it == 4 }
        )
        assertEquals(
            listOf(listOf(4), listOf(4), listOf()),
            listOf(4, 4).breakAfter { it == 4 }
        )
        assertEquals(
            listOf(listOf(4), listOf(5), listOf(6), listOf()),
            listOf(4, 5, 6).breakAfter { it >= 4 }
        )
        assertEquals(
            listOf(listOf(1, 2, 3)),
            listOf(1, 2, 3).breakAfter { it == 4 }
        )
        assertEquals(
            listOf(listOf(1), listOf(2, 3)),
            listOf(1, 2, 3).breakAfter { it == 1 }
        )
    }

    @Test
    fun breakBeforeFirst() {
        assertEquals(
            Pair(listOf(), listOf()),
            listOf<Int>().breakBeforeFirst { it > 3 }
        )
        assertEquals(
            Pair(listOf(), listOf(3)),
            listOf(3).breakBeforeFirst { it == 3 }
        )
        assertEquals(
            Pair(listOf(), listOf(3, 3)),
            listOf(3, 3).breakBeforeFirst { it == 3 }
        )
        assertEquals(
            Pair(listOf(1, 2, 3), listOf()),
            listOf(1, 2, 3).breakBeforeFirst { it == 10 }
        )
        assertEquals(
            Pair(listOf(1, 2, 3), listOf(4, 3, 5)),
            listOf(1, 2, 3, 4, 3, 5).breakBeforeFirst { it > 3 }
        )
    }

    @Test
    fun splitOn() {
        assertEquals(
            listOf(),
            listOf<Int>().splitOn { it == 3 }
        )
        assertEquals(
            listOf(listOf(1)),
            listOf(1).splitOn { it == 3 }
        )
        assertEquals(
            listOf(listOf(1, 2), listOf(4)),
            listOf(1, 2, 3, 4).splitOn { it == 3 }
        )
        assertEquals(
            listOf(listOf(1, 2), listOf(), listOf()),
            listOf(1, 2, 3, 3).splitOn { it == 3 }
        )
        assertEquals(
            listOf(listOf(), listOf(), listOf()),
            listOf(3, 3).splitOn { it == 3 }
        )
    }

    @Test
    fun takeUntil() {
        assertEquals(
            listOf(),
            listOf<Int>().takeUntil { it < 5 },
        )
        assertEquals(
            listOf(5),
            listOf(5).takeUntil { it < 5 },
        )
        assertEquals(
            listOf(5),
            listOf(5, 5).takeUntil { it < 5 },
        )
        assertEquals(
            listOf(4, 2, 4),
            listOf(4, 2, 4).takeUntil { it < 5 },
        )

        assertEquals(
            listOf(4, 2, 5),
            listOf(4, 2, 5).takeUntil { it < 5 },
        )
        assertEquals(
            listOf(4, 2, 5),
            listOf(4, 2, 5, 1).takeUntil { it < 5 },
        )
        assertEquals(
            listOf(4, 2, 5),
            listOf(4, 2, 5, 5, 5, 5, 5, 5).takeUntil { it < 5 },
        )

    }

    @Test
    fun takeWhileSecond() {
        assertEquals(
            listOf(),
            listOf<Int>().takeWhileSecond { it < 2 }
        )
        assertEquals(
            listOf(1),
            listOf(1).takeWhileSecond { it < 2 }
        )
        assertEquals(
            listOf(1, 2),
            listOf(1, 2).takeWhileSecond { it < 2 }
        )
        assertEquals(
            listOf(1, 2),
            listOf(1, 2, 3).takeWhileSecond { it < 2 }
        )
        assertEquals(
            listOf(3),
            listOf(3).takeWhileSecond { it < 2 }
        )
        assertEquals(
            listOf(2),
            listOf(2, 2).takeWhileSecond { it < 2 }
        )
        assertEquals(
            listOf(1, 2),
            listOf(1, 2, 3, 2, 4).takeWhileSecond { it < 2 }
        )
    }
}
