package day2

import aoc.utils.decode
import aoc.utils.getLooping
import aoc.utils.readInput

// S = scissors
// R = rock
// P = paper
// wins suite before it on list and loses to suite after it
// loop if out of bounds
val suites = listOf("R", "P", "S")
val opponent = listOf("A", "B", "C")
val us = listOf("X", "Y", "Z")


fun part1(): Int {
    return readInput("day2-part1.txt")
        .map { it.split(" ") }
        .map {
            Pair(
                suites.decode(it[0], opponent),
                suites.decode(it[1], us),
            )
        }
        .sumOf { calculateScore(it) }
}

fun part2(): Int {
    return readInput("day2-part1.txt")
        .map { it.split(" ") }
        .map {
            Pair(
                suites.decode(it[0], opponent),
                it[1],
            )
        }
        .map {
            Pair(
                it.first,
                when (it.second) {
                    "Z" -> suites.getLooping(suites.indexOf(it.first)+1)
                    "X" -> suites.getLooping(suites.indexOf(it.first)-1)
                    else -> it.first
                }
            )
        }
        .sumOf { calculateScore(it) }
}

fun calculateScore(it: Pair<String, String>): Int {
    val shapeScore = suites.indexOf(it.second) + 1
    val wasDraw = it.first == it.second

    val ourSelection = suites.indexOf(it.second)
    val wasWin = !wasDraw &&
            suites.getLooping(ourSelection - 1) == it.first

    return shapeScore + if (wasWin) 6 else if (wasDraw) 3 else 0;
}

