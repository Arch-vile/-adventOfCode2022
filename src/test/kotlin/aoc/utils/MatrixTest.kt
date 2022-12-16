package aoc.utils

import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class MatrixTest {

    @Test
    fun fromString() {
        assertEquals(
            Matrix(
                listOf(
                    listOf("1", "2"), listOf("3", "4")
                )
            ), Matrix.fromString(listOf("12", "34"))
        )
    }

    @Test
    fun tile() {
        val source = matrix(
            "1234",
            "5678"
        )

        assertEquals(
            matrix(
                "23",
                "67"
            ),
            source.tile(Cursor(1, 0), Cursor(2, 1))
        )

        assertEquals(
            matrix(
                "23",
                "67",
                "88"
            ),
            source.tile(Cursor(1, 0), Cursor(2, 2), "8")
        )

        assertEquals(
            matrix(
                "88",
                "88"
            ),
            source.tile(Cursor(10, 0), Cursor(11, 1), "8")
        )
    }

    @Test
    fun tiles_window() {
        val source = Matrix(
            listOf(
                listOf(10, 11, 12, 13, 14, 15, 16),
                listOf(20, 21, 22, 23, 24, 25, 26),
                listOf(30, 31, 32, 33, 34, 35, 36),
                listOf(40, 41, 42, 43, 44, 45, 46),
            )
        )

        val tiles = source.windowed(Size(3, 2), Size(1, 2))

        /*
         10, 11, 12, 13, 14, 15, 16
         20, 21, 22, 23, 24, 25, 26
         30, 31, 32, 33, 34, 35, 36
         40, 41, 42, 43, 44, 45, 46

         XX, XX, XX, 00, 00, 00, 00
         XX, XX, XX, 00, 00, 00, 00
         00, 00, 00, 00, 00, 00, 00
         00, 00, 00, 00, 00, 00, 00
         */
        assertEquals(
            Matrix(
                listOf(
                    listOf(10, 11, 12), listOf(20, 21, 22)
                )
            ), tiles[0]
        )

        /*
         10, 11, 12, 13, 14, 15, 16
         20, 21, 22, 23, 24, 25, 26
         30, 31, 32, 33, 34, 35, 36
         40, 41, 42, 43, 44, 45, 46

         00, 00, 00, 00, XX, XX, XX
         00, 00, 00, 00, XX, XX, XX
         00, 00, 00, 00, 00, 00, 00
         00, 00, 00, 00, 00, 00, 00
         */
        assertEquals(
            Matrix(
                listOf(
                    listOf(14, 15, 16), listOf(24, 25, 26)
                )
            ), tiles[4]
        )

        /*
            10, 11, 12, 13, 14, 15, 16
            20, 21, 22, 23, 24, 25, 26
            30, 31, 32, 33, 34, 35, 36
            40, 41, 42, 43, 44, 45, 46

            00, 00, 00, 00, 00, 00, 00
            00, 00, 00, 00, 00, 00, 00
            XX, XX, XX, 00, 00, 00, 00
            XX, XX, XX, 00, 00, 00, 00
            */
        assertEquals(
            Matrix(
                listOf(
                    listOf(30, 31, 32), listOf(40, 41, 42)
                )
            ), tiles[5]
        )

        /*
            10, 11, 12, 13, 14, 15, 16
            20, 21, 22, 23, 24, 25, 26
            30, 31, 32, 33, 34, 35, 36
            40, 41, 42, 43, 44, 45, 46

            00, 00, 00, 00, 00, 00, 00
            00, 00, 00, 00, 00, 00, 00
            00, XX, XX, XX, 00, 00, 00
            00, XX, XX, XX, 00, 00, 00
            */
        assertEquals(
            Matrix(
                listOf(
                    listOf(31, 32, 33), listOf(41, 42, 43)
                )
            ), tiles[6]
        )

        /*
         10, 11, 12, 13, 14, 15, 16
         20, 21, 22, 23, 24, 25, 26
         30, 31, 32, 33, 34, 35, 36
         40, 41, 42, 43, 44, 45, 46

         00, 00, 00, 00, 00, 00, 00
         00, 00, 00, 00, 00, 00, 00
         00, 00, 00, 00, XX, XX, XX
         00, 00, 00, 00, XX, XX, XX
         */
        assertEquals(
            Matrix(
                listOf(
                    listOf(34, 35, 36), listOf(44, 45, 46)
                )
            ), tiles.last()
        )
    }


    @Test
    fun equals() {
        assertEquals(
            Matrix(listOf(listOf(1, 2), listOf(3, 4), listOf(5, 6))),
            Matrix(listOf(listOf(1, 2), listOf(3, 4), listOf(5, 6))),
        )

        assertNotEquals(
            Matrix(listOf(listOf(1, 2), listOf(3, 4), listOf(5, 6))),
            Matrix(listOf(listOf(1, 2), listOf(1, 4), listOf(5, 6))),
        )

    }

    @Test
    fun rotate() {
        assertEquals(
            Matrix(
                listOf(
                    listOf(1, 2), listOf(3, 4), listOf(5, 6)
                )
            ),
            Matrix(
                listOf(
                    listOf(2, 4, 6),
                    listOf(1, 3, 5),
                )
            ).rotateCW(),
        )

        assertEquals(
            Matrix(
                listOf(
                    listOf(2, 4, 6),
                    listOf(1, 3, 5),
                )
            ),
            Matrix(
                listOf(
                    listOf(2, 4, 6),
                    listOf(1, 3, 5),
                )
            ).rotateCW().rotateCW().rotateCW().rotateCW(),
        )

    }

    @Test
    fun flipHorizontal() {
        assertEquals(
            Matrix(
                listOf(
                    listOf(2, 4, 6),
                    listOf(1, 3, 5),
                )
            ), Matrix(
                listOf(
                    listOf(1, 3, 5),
                    listOf(2, 4, 6),
                )
            ).flipHorizontal()
        )
    }

    @Test
    fun flipVertical() {
        assertEquals(
            Matrix(
                listOf(
                    listOf(2, 4, 6),
                    listOf(1, 3, 5),
                )
            ), Matrix(
                listOf(
                    listOf(6, 4, 2),
                    listOf(5, 3, 1),
                )
            ).flipVertical()
        )
    }

    @Test
    fun combine_sameSize() {
        val current = Matrix(
            listOf(
                listOf(1, 2, 4),
                listOf(3, 5, 6),
            )
        )

        current.combine(
            Matrix(
                listOf(
                    listOf("2", "3", "2"),
                    listOf("0", "0", "1"),
                )
            )
        ) { a, b -> (a.value.toString() + b.value).toInt() }

        assertEquals(
            Matrix(
                listOf(
                    listOf(12, 23, 42),
                    listOf(30, 50, 61),
                )
            ), current
        )
    }

    @Test
    fun combine_different_size() {
        val current = Matrix(
            listOf(
                listOf(1, 2, 4),
                listOf(3, 5, 6),
            )
        )

        current.combine(
            Matrix(
                listOf(
                    listOf("3", "2", "9"),
                    listOf("0", "1", "9"),
                )
            ), Cursor(1, 1)
        ) { a, b -> (a.value.toString() + b.value).toInt() }

        assertEquals(
            Matrix(
                listOf(
                    listOf(1, 2, 4),
                    listOf(3, 53, 62),
                )
            ), current
        )
    }

    @Test
    fun readWithFilling() {
        val matrix = Matrix(listOf(
            listOf(1, 2),
            listOf(3, 5, 6),
        ), { x, y -> 666 })

        assertEquals(
            Matrix(
                listOf(
                    listOf(1, 2, 666),
                    listOf(3, 5, 6),
                )
            ), matrix
        )
    }

    @Test
    fun trace() {
        val matrix = Matrix(
            listOf(
                listOf(10, 11, 12, 13, 14, 15, 16),
                listOf(20, 21, 22, 23, 24, 25, 26),
                listOf(30, 31, 32, 33, 34, 35, 36),
                listOf(40, 41, 42, 43, 44, 45, 46),
            )
        )

        assertEquals(listOf(20, 30, 40), matrix.trace(Cursor(0, 0)) { current ->
            Cursor(current.x, current.y + 1)
        }.map { it.value })
        assertEquals(listOf(20, 10), matrix.trace(Cursor(0, 2)) { current ->
            Cursor(current.x, current.y - 1)
        }.map { it.value })

        assertEquals(listOf(21, 32, 43), matrix.trace(Cursor(0, 0)) { current ->
            Cursor(current.x + 1, current.y + 1)
        }.map { it.value })

        var count = 0
        assertEquals(listOf(20, 30), matrix.trace(Cursor(0, 0)) { current ->
            count++
            if (count <= 2) Cursor(current.x, current.y + 1)
            else null
        }.map { it.value })

    }

    @Test
    fun toStringTest() {
        val m = Matrix(20, 20) { x, y -> "0" }
        println(m.visualize(""))
    }

    fun matrix(vararg lines: String): Matrix<String> {
        return Matrix.fromString(lines.toList())
    }
}
