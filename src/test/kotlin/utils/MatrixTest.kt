package utils

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class MatrixTest {

    @Test
    fun tile() {
        val source = Matrix(
            listOf(
                listOf(0, 0, 1, 0),
                listOf(0, 1, 1, 0)
            )
        )

        assertEquals(
            Matrix(
                listOf(
                    listOf(0, 1),
                    listOf(1, 1)
                )
            ),
            source.tile(Point(1, 0), Point(2, 1))
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
                    listOf(10, 11, 12),
                    listOf(20, 21, 22)
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
                    listOf(14, 15, 16),
                    listOf(24, 25, 26)
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
                    listOf(30, 31, 32),
                    listOf(40, 41, 42)
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
                    listOf(31, 32, 33),
                    listOf(41, 42, 43)
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
                    listOf(34, 35, 36),
                    listOf(44, 45, 46)
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
                    listOf(1, 2),
                    listOf(3, 4),
                    listOf(5, 6)
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
            ),
            Matrix(
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
            ),
            Matrix(
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

        current.combine(Matrix(
            listOf(
                listOf("2", "3", "2"),
                listOf("0", "0", "1"),
            )
        )) { a, b -> (a.value.toString() + b.value).toInt()}

        assertEquals(
            Matrix(
                listOf(
                    listOf(12, 23, 42),
                    listOf(30, 50, 61),
                )
            ),
            current
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

        current.combine(Matrix(
            listOf(
                listOf("3", "2","9"),
                listOf("0", "1","9"),
            )
        ), Cursor(1,1)) { a, b -> (a.value.toString() + b.value).toInt()}

        assertEquals(
            Matrix(
                listOf(
                    listOf(1, 2, 4),
                    listOf(3, 53, 62),
                )
            ),
           current
        )
    }

}
