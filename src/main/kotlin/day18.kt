package day18

import aoc.utils.Point
import aoc.utils.readInput
import kotlin.math.abs


fun main() {

    val rocks = points().toSet()

    val emptySurface = rocks
        .flatMap { rock ->
            val neighbourSpots = neighbours(rock)
            val neighbourRocks = neighbourRocks(rock, rocks)
            val emptyNeighbours = neighbourSpots.minus(neighbourRocks)
            emptyNeighbours
        }.toSet()

    println(emptySurface)

    val toProcess = emptySurface.toMutableSet()
    while (toProcess.isNotEmpty()) {
        val check = toProcess.first()
        toProcess.remove(check)
        val accessible = findAllAccessible(check, emptySurface)
        if (isOuterLayer(accessible, rocks)) {
            println("outer layer: ${accessible.size}")
//            calculateRocks(accessible, rocks).let { println(it) }
//            throw Error("found")
        }
    }
}

fun calculateRocks(accessible: Set<Point>, rocks: Set<Point>): Int {
    return accessible.map {
        neighbourRocks(it, rocks).size
    }.sum()
}

fun isOuterLayer(empties: Set<Point>, rocks: Set<Point>): Boolean {
    empties.forEach { empty ->
        val up = empty.onDirection(rocks, Point(0, 1, 0))
        val down = empty.onDirection(rocks, Point(0, -1, 0))
        val left = empty.onDirection(rocks, Point(-1, 0, 0))
        val right = empty.onDirection(rocks, Point(1, 0, 0))
        val toward = empty.onDirection(rocks, Point(0, 0, 1))
        val away = empty.onDirection(rocks, Point(0, 0, -1))

        //  Can reach outer space
        if (up.isEmpty() || down.isEmpty() ||
            left.isEmpty() || right.isEmpty() ||
            toward.isEmpty() || away.isEmpty()
        )
            return true
    }
    return false
}


fun findAllAccessible(first: Point, emptySurface: Set<Point>): Set<Point> {
    val accessible = mutableSetOf(first)
    val toProcess = mutableSetOf(first)
    val processed = mutableSetOf<Point>()

    while (toProcess.isNotEmpty()) {
        val point = toProcess.first()
        toProcess.remove(point)
        processed.add(point)

        val sees = point.onDirectLine(emptySurface)
        accessible.addAll(sees)
        toProcess.addAll(sees.minus(processed))
    }

    return accessible
}
//    val rocks = points().toSet()
//
//    val emptySurface = rocks
//        .flatMap { rock ->
//            val neighbourSpots = neighbours(rock)
//            val neighbourRocks = neighbourRocks(rock, rocks)
//            val emptyNeighbours = neighbourSpots.minus(neighbourRocks)
//            emptyNeighbours
//        }.toSet()
//
//    val outerSurface = mutableSetOf<Point>()
//    val toCheck = emptySurface.toMutableSet()
//
//    while (toCheck.isNotEmpty()) {
//        val empty = toCheck.first()
//
//        val up = empty.onDirection(rocks, Point(0, 1, 0))
//        val down = empty.onDirection(rocks, Point(0, -1, 0))
//        val left = empty.onDirection(rocks, Point(-1, 0, 0))
//        val right = empty.onDirection(rocks, Point(1, 0, 0))
//        val toward = empty.onDirection(rocks, Point(0, 0, 1))
//        val away = empty.onDirection(rocks, Point(0, 0, -1))
//
//         Can reach outer space
//        if (up.isEmpty() || down.isEmpty() ||
//            left.isEmpty() || right.isEmpty() ||
//            toward.isEmpty() || away.isEmpty()
//        ) {
//            outerSurface.add(empty)
//            val sees = empty.onDirectLine(emptySurface)
//            outerSurface.addAll(sees)
//
//

//        }
//    }


//    println(outerSurface.size)

// 4044 is too low


//}

fun neighbourRocks(rock: Point, rocks: Set<Point>): Set<Point> {
    return rocks.filter {
        it.nextTo(rock)
    }.toSet()
}

fun neighbours(current: Point): Set<Point> {

    return setOf(
        current.copy(x = current.x + 1),
        current.copy(x = current.x - 1),
        current.copy(y = current.y + 1),
        current.copy(y = current.y - 1),
        current.copy(z = current.z + 1),
        current.copy(y = current.z - 1),
    )


}


fun freeSidesOutside(current: Point, points: List<Point>): Int {

    val onSameLine = points.filter { point ->
        val isOnSameLine =
            listOf(abs(current.x - point.x), abs(current.y - point.y), abs(current.z - point.z))
                .filter { it != 0L }.size == 1

        isOnSameLine
    }

    val encountersOnDirection = onSameLine.groupBy { point ->
        // 0,1,0 or 0,-1,0 always only one coordinate is 1 or -1
        "${toDirection(current.x - point.x)}${toDirection(current.y - point.y)}${toDirection((current.z - point.z))}"
    }


    return 6 - encountersOnDirection.keys.size
}

private fun toDirection(value: Long): Long {
    if (value == 0L) return 0
    return value / abs(value)
}

private fun points(): List<Point> {
    val points = readInput("test.txt")
        .map { it.split(",") }
        .map { Point(it[0].toLong(), it[1].toLong(), it[2].toLong()) }
    return points
}

fun part1(): Int {
    val points = points()
    return points
        .map { point ->
            val neighbours = points.filter { it.nextTo(point) }
            6 - neighbours.size
        }
        .sum()
}

fun part2(): Int {
    return 1;
}
