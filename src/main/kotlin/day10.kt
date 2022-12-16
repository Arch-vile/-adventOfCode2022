package day10

import aoc.utils.*

fun part1(): Int {
    val valueByCycle = valuesByCycle("day10-input.txt")

    return (20..220).step(40)
        .map { valueByCycle[it - 1] }
        .sumOf { value -> value.first * value.second }
}

fun part2(): String {
    val valuesByCycle = valuesByCycle("day10-input.txt")
    var crt = ""
    valuesByCycle.map { it.second }.forEachIndexed { i, value ->
        if (closedRange(value - 1, value + 1).contains(i % 40)) {
            crt += "#"
        } else crt += "."
    }
    return crt.windowed(40, 40).joinToString("") { "$it\n" }
}

private fun valuesByCycle(file: String): List<Pair<Int, Int>> {
    var registerX = MutableInt(1)

    // Try with reduce also
    val registerValues = readInput(file).flatMap {
        if (it.firstPart() == "noop") {
            listOf(registerX.value)
        } else {
            listOf(
                registerX.value, registerX.plus(it.secondAsInt())
            )
        }
    }

    return listOf(Pair(1, 1)) + registerValues.mapIndexed { index, value -> Pair(index + 2, value) }
}
