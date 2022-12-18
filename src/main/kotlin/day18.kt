package day18

import aoc.utils.Point
import aoc.utils.readInput

fun main() {
    val points = points()


}

private fun points(): List<Point> {
    val points = readInput("day18-input.txt")
        .map { it.split(",") }
        .map { Point(it[0].toLong(), it[1].toLong(), it[2].toLong()) }
    return points
}

fun part1(): Int {
    val points = points()
    return points
        .map { point ->
            val neighbours = points.filter { it.nextTo(point) }
            6-neighbours.size
        }
        .sum()
}

fun part2(): Int {
    return 1;
}
