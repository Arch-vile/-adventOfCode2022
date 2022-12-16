package utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset

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
    fun sortedLookup_same_value_for_many_keys() {
        val foo = SortedLookup<String, Long>()
        foo.add("foo", 1)
        foo.add("bar", 1)

        assertEquals(
            setOf("foo", "bar"),
            foo.sortedSequence().map { it.first }
        )
    }

    @Test
    fun foo() {

        val output =
            File("/Users/mikko.ravimo@futurice.com/temp/papertrail/jeddahFinalizations_nice.log").bufferedWriter()

        var gameId = ""
        fun out(message: String) {
//            println(message)
            output.write("$gameId $message")
            output.write("\n")
        }

        val lines = read("/Users/mikko.ravimo@futurice.com/temp/papertrail/jeddahFinalizations.log")

        var previous = ""
        for (line in lines.filter { !it.contains("scores_gameActions") }) {

            if (line.contains("Finalization for game")) {
                val gameLine = line.split(" ")
                gameId = gameLine[9]
            }

            if (line.contains("gameActionCreatedAt")) {
                if (previous.contains("GameFinalizationActionDto") && !previous.contains("StatGameFin")) {
                    val timeString =
                        line.split(" ").dropLast(1).last().replace(",", "").split("m")[1].substring(
                            0,
                            13
                        );
                    val datetime = LocalDateTime.ofEpochSecond(
                        timeString.toLong() / 1000,
                        0,
                        ZoneOffset.ofHours(2)
                    )
                    val time = datetime.toLocalTime()
                    out("$line $time")
                }
            }
            else {
                if(!line.contains("gameActionCreatedAt")) {
                    if (line.contains("debug") && line.contains("seen within event"))
                        out(line)
                    if (line.contains("Successfully exported game") || line.contains("Marked"))
                        out(line)
                }
            }


            previous = line

        }


    }

}
