package day8

import aoc.utils.*

fun part1(): Int {
    val input = readInput("day8-input.txt")
        .map { it.toList().map { it.toInt() } }

    val matrix = Matrix(input)

    return matrix.all()
        .filter {
            val lineUp = matrix.trace(it.cursor) { p -> Cursor(p.x, p.y - 1) }
            val lineDown = matrix.trace(it.cursor) { p -> Cursor(p.x, p.y + 1) }
            val lineLeft = matrix.trace(it.cursor) { p -> Cursor(p.x - 1, p.y) }
            val lineRight = matrix.trace(it.cursor) { p -> Cursor(p.x + 1, p.y) }

            allShorter(it.value, lineUp) ||
                    allShorter(it.value, lineDown) ||
                    allShorter(it.value, lineLeft) ||
                    allShorter(it.value, lineRight)
        }
        .count()

}

fun part2(): Int {
    val input = readInput("day8-input.txt")
        .map { it.toList().map { it.toInt() } }

    val matrix = Matrix(input)

    return matrix.all()
        .map {
            val lineUp = matrix.trace(it.cursor) { p -> Cursor(p.x, p.y - 1) }
            val lineDown = matrix.trace(it.cursor) { p -> Cursor(p.x, p.y + 1) }
            val lineLeft = matrix.trace(it.cursor) { p -> Cursor(p.x - 1, p.y) }
            val lineRight = matrix.trace(it.cursor) { p -> Cursor(p.x + 1, p.y) }

            product(
                listOf(
                    viewingDistance(it.value, lineUp),
                    viewingDistance(it.value, lineDown),
                    viewingDistance(it.value, lineLeft),
                    viewingDistance(it.value, lineRight)
                )
            )
        }
        .maxOf{ it}

}

fun viewingDistance(height: Int, line: List<Entry<Int>>): Int {
    if(line.isEmpty())
        return 1

    return line
        .map { it.value }
        .breakAfter { it >= height }[0]
        .size
}

fun allShorter(height: Int, line: List<Entry<Int>>): Boolean {
    return line.filter { it.value >= height }.isEmpty()
}

fun product(it: List<Int>): Int {
    return it.reduce { acc, i -> i * acc }
}
