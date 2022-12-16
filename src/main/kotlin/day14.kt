package day14

import aoc.utils.*
import java.lang.Integer.min

fun main() {
    part1()
}

fun part1(): Int {
    val sandSource = Cursor(500, 0)
    val rockLinEnds = readInput("test.txt")
        .map { it.findInts().windowed(2, 2).map { Cursor(it[0], it[1]) } }

    val smallestX = rockLinEnds.map { it.minOf { min(it.x, sandSource.x) } }.minOrNull()!!
    val smallestY = rockLinEnds.map { it.minOf { min(it.y, sandSource.y) } }.minOrNull()!!
    val offset = Cursor(smallestX, smallestY)

    val offsettedEnds = rockLinEnds.map { it.map { it.minus(offset) } }
    val largestX = offsettedEnds.map { it.maxOf { it.x } }.maxOrNull()!!
    val largestY = offsettedEnds.map { it.maxOf { it.y } }.maxOrNull()!!

    val cave = Matrix(largestX.toLong() + 1, largestY.toLong() + 1) { i: Int, i2: Int -> '.' }
    cave.replace(Cursor(500, 0).minus(offset)) { entry -> '+' }

    val rockLines = offsettedEnds
        .flatMap { it.windowed(2, 1).map { it[0] to it[1] } }

    rockLines.forEach {
        drawRockLine(cave, it)
    }

    println(cave.visualize(""))



    return 1;
}

fun drawRockLine(cave: Matrix<Char>, line: Pair<Cursor, Cursor>) {
    val start = line.first
    var end = line.second
    val line = Line(Point(start.x.toLong(), start.y.toLong(), 0), Point(end.x.toLong(), end.y.toLong(), 0))

    val points = pointsInLine(line)
        .map { Cursor(it.x.toInt(), it.y.toInt()) }

    points.forEach {
        cave.replace(it){ '#'}
    }
}


fun part2(): Int {
    return 1;
}
