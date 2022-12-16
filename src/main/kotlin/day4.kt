package day4

import utils.readInput

fun part1(): Int {
return    readInput("day4-input.txt")
        .map { it.split(",") }
        .map {
            Pair(
                toRange(it[0]),
                toRange(it[1])
            )
        }
        .filter { overlap(it.first, it.second)  }
        .count()
}

fun overlap2(first: Pair<Int, Int>, second: Pair<Int, Int>): Boolean {
    if(first.first >= second.first && first.first <= second.second)
        return true

    return false

}
fun overlap(first: Pair<Int, Int>, second: Pair<Int, Int>): Boolean {
    if (first.first >= second.first && first.second <= second.second)
        return true

    if (second.first >= first.first && second.second <= first.second)
        return true;

    return false
}

fun toRange(s: String): Pair<Int, Int> {
    val parts = s.split("-")
    return Pair(parts[0].toInt(), parts[1].toInt())
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
        .filter { overlap2(it.first, it.second) || overlap2(it.second, it.first) }
        .count()
}
