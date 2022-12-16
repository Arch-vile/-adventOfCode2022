package utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CollectionsKtTest {


    @Test
    fun listLooping() {
        assertEquals("B", listOf("A","B").getLooping(-1))
        assertEquals("A", listOf("A","B").getLooping(-2))
        assertEquals("A", listOf("A","B").getLooping(0))
        assertEquals("B", listOf("A","B").getLooping(1))
        assertEquals("A", listOf("A","B").getLooping(2))
    }

    @Test
    fun decode() {
        assertEquals("A", listOf("A","B").decode("X", listOf("X","Y")))
        assertEquals("A", listOf("A","B").decode(1, listOf(1,2)))

        assertEquals("X", listOf("A","B").encode("A", listOf("X","Y")))
        assertEquals(1, listOf("A","B").encode("A", listOf(1,2)))
    }

    @Test
    fun combinations() {
        assertEquals(
            setOf(
                listOf(1),
                listOf(2),
                listOf(3),
                listOf(1, 2),
                listOf(1, 3),
                listOf(2, 3),
                listOf(1, 2, 3),
            ),
            combinations(listOf(1, 2, 3)),
        )
    }

    @Test
    fun combinations_with_duplicates() {
        assertEquals(
            setOf(
                listOf(1),
                listOf(2),
                listOf(1, 2),
                listOf(2, 2),
                listOf(1, 2, 2),
            ),
            combinations(listOf(1, 2, 2)),
        )
    }

    @Test
    fun permutations() {
        assertEquals(
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
    fun sortedLookup_override_values() {
        val foo = SortedLookup<String, Long>()
        foo.add("foo", 1)
        foo.add("foo", 3)
        assertEquals(3, foo.get("foo"))
    }

    @Test
    fun sortedLookup_values_ordered() {
        val foo = SortedLookup<String, Long>()
        foo.add("a", 1)
        foo.add("b", 3)
        foo.add("c", 2)

        assertEquals(listOf(1,2), listOf(1,2))
        // Damn not sure why assertting lists did not work here. well cast to string
        assertEquals( "1, 2, 3" ,foo.sortedByValue().toList().map { it.second }.joinToString())
    }
    @Test
    fun sortedLookup_same_value_for_many_keys() {
        val foo = SortedLookup<String, Long>()
        foo.add("foo", 1)
        foo.add("bar", 1)

        assertEquals(
            // Values same, sorted by key
            listOf("bar","foo" ),
            foo.sortedByValue().map { it.first }.toList()
        )
    }


}
