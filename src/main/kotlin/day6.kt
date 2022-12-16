package day6

import utils.readInput

fun part1(): Int {
    val marker = readInput("day6-input.txt")
        .windowed(4,1)
        .first { it.intersect(it).size == 4 }
        .joinToString("")
    return readInput("day6-input.txt")[0].indexOf(marker)
}

fun part2(): Int {
    return 1;
}
