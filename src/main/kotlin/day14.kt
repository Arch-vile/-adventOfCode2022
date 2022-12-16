package day14

import aoc.utils.*
import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    part2()
}

fun part2(): Int {
    val sandSource = Cursor(500, 0)
    var rockLinEnds = readInput("day14-input.txt")
        .map { it.findInts().windowed(2, 2).map { Cursor(it[0], it[1]) } }

    val large = rockLinEnds.map { it.maxOf { max(it.y, sandSource.y) } }.maxOrNull()!!
    val small = rockLinEnds.map { it.minOf { min(it.x, sandSource.x) } }.minOrNull()!!
    rockLinEnds = rockLinEnds
        .plus(element = listOf(Cursor(small-10000,large+2),Cursor(small+10000,large+2)))

    val smallestX = rockLinEnds.map { it.minOf { min(it.x, sandSource.x) } }.minOrNull()!!
    val smallestY = rockLinEnds.map { it.minOf { min(it.y, sandSource.y) } }.minOrNull()!!

    val offset = Cursor(smallestX, smallestY)

    val offsettedEnds = rockLinEnds.map { it.map { it.minus(offset) } }
    val largestOffsetX = offsettedEnds.map { it.maxOf { it.x } }.maxOrNull()!!
    val largestOffsetY = offsettedEnds.map { it.maxOf { it.y } }.maxOrNull()!!

    val cave = Matrix(largestOffsetX.toLong() + 1, largestOffsetY.toLong() + 1) { i: Int, i2: Int -> '.' }
    cave.replace(Cursor(500, 0).minus(offset)) { entry -> '+' }

    val rockLines = offsettedEnds
        .flatMap { it.windowed(2, 1).map { it[0] to it[1] } }

    rockLines.forEach {
        drawRockLine(cave, it)
    }

    for (i in 1..930000) {

        var sand: Cursor? = sandSource.minus(offset)
        while(sand != null) {
            val down = Cursor(0, 1)
            val downLeft = Cursor(-1, 1)
            val downRight = Cursor(1, 1)

            if (cave.getRelative(sand, down) == null || cave.getRelative(sand, down)?.value == '.') {
                sand = sand.move(down)
            } else if (cave.getRelative(sand, downLeft) == null || cave.getRelative(sand, downLeft)?.value == '.') {
                sand = sand.move(downLeft)
            } else if (cave.getRelative(sand, downRight) == null || cave.getRelative(sand, downRight)?.value == '.') {
                sand = sand.move(downRight)
            } else if(cave.isInBounds(sand)){

                if(sand==sandSource.minus(offset)) {
                    throw Error("full at ${i}")
                }

                cave.replace(sand) { 'o' }
                sand = null
            }

//            if(sand != null && !cave.isInBounds(sand)) {
//                throw Error("Out of bounds on round ${i-1}")
//            }
        }


    }

//    println(cave.visualize(""))

    // 874 your answer is too high.
    // 873 correct...
    return 1;
}
fun part1(): Int {
    val sandSource = Cursor(500, 0)
    val rockLinEnds = readInput("day14-input.txt")
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

    for (i in 1..10000) {

        var sand: Cursor? = sandSource.minus(offset)
        while(sand != null) {
            val down = Cursor(0, 1)
            val downLeft = Cursor(-1, 1)
            val downRight = Cursor(1, 1)

            if (cave.getRelative(sand, down) == null || cave.getRelative(sand, down)?.value == '.') {
                sand = sand.move(down)
            } else if (cave.getRelative(sand, downLeft) == null || cave.getRelative(sand, downLeft)?.value == '.') {
                sand = sand.move(downLeft)
            } else if (cave.getRelative(sand, downRight) == null || cave.getRelative(sand, downRight)?.value == '.') {
                sand = sand.move(downRight)
            } else if(cave.isInBounds(sand)){
                cave.replace(sand) { 'o' }
                sand = null
            }

            if(sand != null && !cave.isInBounds(sand)) {
                throw Error("Out of bounds on round ${i-1}")
            }
        }


    }

    println(cave.visualize(""))

    // 874 your answer is too high.
    // 873 correct...
    return 1;
}

fun drawRockLine(cave: Matrix<Char>, line: Pair<Cursor, Cursor>) {
    val start = line.first
    var end = line.second
    val line = Line(Point(start.x.toLong(), start.y.toLong(), 0), Point(end.x.toLong(), end.y.toLong(), 0))

    val points = pointsInLine(line)
        .map { Cursor(it.x.toInt(), it.y.toInt()) }

    points.forEach {
        cave.replace(it) { '#' }
    }
}


