package utils

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GeoTest {

    @Test
    fun diagonalLines() {
       assertEquals(
           listOf(
               Point(0,0),
               Point(1,1),
               Point(2,2),
           ),
        pointsInLine(Line(Point(0,0), Point(2,2))))

        assertEquals(
            listOf(
                Point(2,0),
                Point(1,1),
                Point(0,2),
            ),
            pointsInLine(Line(Point(2,0), Point(0,2))))
    }

}
