package utils

import java.lang.Long.max
import java.lang.Long.min
import kotlin.math.pow

data class Point(val x: Long, val y: Long, val z: Long = 0) {

    fun distance(to: Point) =
        ((x.toDouble() - to.x).pow(2.0) +
                (y.toDouble() - to.y).pow(2.0) +
                (z.toDouble() - to.z).pow(2.0)).pow(0.5);

}
data class Line(val start: Point, val end: Point)

// Only for horizontal, vertical and 45 degree lines for now
fun pointsInLine(line: Line): List<Point> {
    // Horizontal/vertical
    if (line.start.x == line.end.x || line.start.y == line.end.y) {
        return (min(line.start.y, line.end.y)..max(line.start.y, line.end.y))
            .flatMap { y ->
                (min(line.start.x, line.end.x)..max(line.start.x, line.end.x))
                    .map { x ->
                        Point(x, y)
                    }
            }
    }
    // 45 degrees
    else {
        val xValues =
            if (line.start.x > line.end.x) line.start.x downTo line.end.x
        else  (line.start.x..line.end.x)

        var yValues =
            if(line.start.y > line.end.y) line.start.y downTo line.end.y
        else (line.start.y..line.end.y)

        return yValues.toList().zip(xValues.toList()).map { Point(it.second, it.first) }
    }
}
