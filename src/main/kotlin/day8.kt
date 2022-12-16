package day8

import aoc.utils.Matrix
import aoc.utils.readInput
import aoc.utils.toList

data class Tree(val name: String, val hight: Int)

fun part1(): Int {
    val input = readInput("day8practice-input.txt")
        .map { it.toList().map { it.toInt() } }

    val trees = input.mapIndexed { y, row -> row.mapIndexed { x, column -> Tree("$y$x", column)  }  }

    val inLines = countInLines(trees)
    println(inLines)

    val inRows = countInLines(Matrix(trees).rotateCW().values())
    println(inRows)

    val combined = inLines.union(inRows)
    println(combined)

    // 1121 not correct
    // 1531 not correct
    return combined.size + input.size*2 + (input[0].size-2)*2;
}

private fun countInLines(input: List<List<Tree>>): MutableSet<Tree> {
    val trees = mutableSetOf<Tree>()

    for (y in 1..input.size - 2) {
        for (x in 1..input[0].size - 2) {

            val currentRow = input[y]
            val before = currentRow.subList(0, x)
            val after = currentRow.subList(x + 1, currentRow.size)

            if (before.maxOf { it.hight } < input[y][x].hight || after.maxOf { it.hight } < input[y][x].hight)
                trees.add(input[y][x])
        }
    }
    return trees
}

fun part2(): Int {
    return 1;
}
