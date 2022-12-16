package day9

import aoc.utils.Cursor
import aoc.utils.firstPart
import aoc.utils.readInput
import aoc.utils.secondPart
import kotlin.math.abs

fun part1(): Int {
    var head = Cursor(0, 0)

    val directions = readInput("day9-input.txt")
        .map { Pair(it.firstPart(), it.secondPart().toInt()) }

    val knotMoves = runSimulation(head,directions)
//    tailHistory.forEach { println(it) }
    return knotMoves.toSet().size;
}

fun runSimulation(start: Cursor, directions: List<Pair<String, Int>>): MutableList<Cursor> {
    var head = start
    var tail = start
    val tailHistory = mutableListOf(tail)

    directions.forEach { direction ->
        head = move(direction, head)

        // If needs diagonal move
        while (
            head.x != tail.x && head.y != tail.y && isNotAdjecant(tail, head)
        ) {
            if (head.x > tail.x && head.y > tail.y) {
                tail = tail.copy(x = tail.x + 1, y = tail.y + 1)
            } else if (head.x > tail.x && head.y < tail.y) {
                tail = tail.copy(x = tail.x + 1, y = tail.y - 1)
            } else if (head.x < tail.x && head.y > tail.y) {
                tail = tail.copy(x = tail.x - 1, y = tail.y + 1)
            } else if (head.x < tail.x && head.y < tail.y) {
                tail = tail.copy(x = tail.x - 1, y = tail.y - 1)
            }
            tailHistory.add(tail)
        }

        // Now they are in same row or column
        // Same row
        while (head.x != tail.x && isNotAdjecant(tail, head)) {
            if (head.x > tail.x)
                tail = tail.copy(x = tail.x + 1)
            else if (head.x < tail.x)
                tail = tail.copy(x = tail.x - 1)
            tailHistory.add(tail)
        }

        // Same column
        while (head.y != tail.y && isNotAdjecant(tail, head)) {
            if (head.y > tail.y)
                tail = tail.copy(y = tail.y + 1)
            else if (head.y < tail.y)
                tail = tail.copy(y = tail.y - 1)
            tailHistory.add(tail)
        }
    }
    return tailHistory
}

fun isNotAdjecant(tail: Cursor, head: Cursor): Boolean {
    return !(abs(tail.x - head.x) <= 1 && abs(tail.y - head.y) <= 1)
}

private fun move(it: Pair<String, Int>, point: Cursor): Cursor {
    return when (it.first) {
        "U" -> point.copy(y = point.y + it.second)
        "D" -> point.copy(y = point.y - it.second)
        "R" -> point.copy(x = point.x + it.second)
        "L" -> point.copy(x = point.x - it.second)
        else -> throw Error("unhandled")
    }
}

fun part2(): Int {
    return 1;
}
