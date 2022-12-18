package aoc.utils

import java.lang.Long.max
import java.lang.Long.min
import kotlin.math.abs
import kotlin.math.pow

data class Point(val x: Long, val y: Long, val z: Long = 0) {

    fun distance(to: Point) =
        ((x.toDouble() - to.x).pow(2.0) +
                (y.toDouble() - to.y).pow(2.0) +
                (z.toDouble() - to.z).pow(2.0)).pow(0.5);

    // Not diagonally
    fun nextTo(point: Point): Boolean {
        return abs(x-point.x)+abs(y-point.y)+abs(z-point.z) == 1L
    }

    fun minus(it: Point): Point {
        return copy(x=x-it.x,y=y-it.y,z=z-it.z)
    }

    // e.g. (3,0,-2) -> (1,0,-1)
    fun unitComponents(): Point {
        return copy(
            x= if(x==0L) 0 else x/ abs(x),
            y= if(y==0L) 0 else y/abs(y),
            z= if(z==0L) 0 else z/abs(z)
        )
    }

    /**
     * which of the given points are in the given direction e.g. (0,-1,0)
     */
    fun onDirection(points: Set<Point>, direction: Point): Set<Point> {
        return points.filter {
            minus(it).unitComponents() == direction
        }.toSet()
    }


    // Points on direct line to any 6 directions
    fun onDirectLine(points: Set<Point>): Set<Point> {
        return points.filter { point ->
            val isOnSameLine =
                listOf(abs(x - point.x), abs(y - point.y), abs(z - point.z))
                    .filter { it != 0L }.size == 1

            isOnSameLine
        }.toSet()
    }
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


