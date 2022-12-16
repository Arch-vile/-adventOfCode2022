package day1

import utils.readInput
import utils.splitOn

fun part1(): Int {
    return readInput("day1-part1.txt")
        .splitOn("")
        .map { it.sumOf { it.toInt() } }
        .maxOf { it }
}
