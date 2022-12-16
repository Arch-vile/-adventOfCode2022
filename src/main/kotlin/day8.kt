package day8

import aoc.utils.Matrix
import aoc.utils.readInput
import aoc.utils.toList

fun part1(): Int {
    val input = readInput("day8practice-input.txt")
        .map { it.toList().map { it.toInt() } }

    val inLines = countInLines(input)
    println(inLines)

    val inRows = countInLines(Matrix(input).rotateCW().values())
    println(inRows)

    val combined = inLines.union(inRows)
    println(combined)

    // 1121 not correct
    return combined.size + input.size*2 + (input[0].size-2)*2;
}

private fun countInLines(input: List<List<Int>>): MutableSet<Pair<Int, Int>> {
    val trees = mutableSetOf<Pair<Int, Int>>()

    for (y in 1..input.size - 2) {
        for (x in 1..input[0].size - 2) {

            val currentRow = input[y]
            val before = currentRow.subList(0, x)
            val after = currentRow.subList(x + 1, currentRow.size)

            if (before.maxOf { it } < input[y][x] || after.maxOf { it } < input[y][x])
                trees.add(Pair(y, x))
        }
    }
    return trees
}

fun part2(): Int {
    return 1;
}
