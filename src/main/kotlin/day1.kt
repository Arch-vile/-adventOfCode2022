package day1

import aoc.utils.readInput
import aoc.utils.splitOn

fun part1(): Int {
    return readInput("day1-part1.txt")
        .splitOn { it == "" }
        .map { it.sumOf { it.toInt() } }
        .maxOf { it }
}

fun part2(): Int {
    return readInput("day1-part1.txt")
        .splitOn { it == "" }
        .map { it.sumOf { it.toInt() } }
        .sortedDescending()
        .take(3)
        .sum()
}
