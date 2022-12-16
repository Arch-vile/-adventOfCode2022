package day4

import aoc.utils.closedRange
import aoc.utils.containsFully
import aoc.utils.hasOverlap
import aoc.utils.readInput

fun part1(): Int {
return    readInput("day4-input.txt")
        .map { it.split(",") }
        .map {
            Pair(
                toRange(it[0]),
                toRange(it[1])
            )
        }
        .filter { containsFullyEither(it.first, it.second)  }
        .count()
}

fun containsFullyEither(first: ClosedRange<Int>, second: ClosedRange<Int>): Boolean {
   return first.containsFully(second) || second.containsFully(first)
}

fun toRange(s: String): ClosedRange<Int> {
    val parts = s.split("-")
    return closedRange(parts[0].toInt(), parts[1].toInt())
}

fun part2(): Int {
    return    readInput("day4-input.txt")
        .map { it.split(",") }
        .map {
            Pair(
                toRange(it[0]),
                toRange(it[1])
            )
        }
        .filter { it.first.hasOverlap(it.second) }
        .count()
}
