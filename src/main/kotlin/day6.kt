package day6

import utils.readInput

fun part1(): Int {
    val input = readInput("day6-input.txt")[0]
    val marker =
        input
            .toCharArray().toList()
            .windowed(4, 1)
            .first { it.intersect(it).size == 4 }
            .joinToString("")

    return input.indexOf(marker) + 4
}

fun part2(): Int {
    val input = readInput("day6-input.txt")[0]
    val marker =
        input
            .toCharArray().toList()
            .windowed(14, 1)
            .first { it.intersect(it).size == 14 }
            .joinToString("")

    return input.indexOf(marker) + 14}
