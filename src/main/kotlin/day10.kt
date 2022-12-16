package day10

import aoc.utils.*

fun part1(): Int {

    var registerX = MutableInt(1)

    // Try with reduce also
    val registerValues =
        readInput("day10-input.txt")
            .flatMap {
                if (it.firstPart() == "noop") {
                    listOf(registerX.value)
                } else {
                    listOf(
                        registerX.value,
                        registerX.plus(it.secondAsInt())
                    )
                }
            }

    val valueByCycle =
        listOf( Pair(1,1)) +
        registerValues.mapIndexed { index, value -> Pair(index + 2, value) }

    return listOf(
        valueByCycle[20 - 1],
        valueByCycle[60 - 1],
        valueByCycle[100 - 1],
        valueByCycle[140 - 1],
        valueByCycle[180 - 1],
        valueByCycle[220 - 1]
    ).sumOf { value -> value.first * value.second }
}


fun part2(): String {

    var cycle = 1
    var register = 1
    var executing = mutableListOf<String>()
    var crtRow = mutableListOf<String>()

    var crtResult = mutableListOf<String>()


    val result = listOf(Pair(0, register)).plus(readInput("day10-input.txt")
        .flatMap {

            if (it.firstPart() == "noop") {
                executing += "noop"
            }

            if (it.firstPart() == "addx") {
                executing += "noop"
                executing += it
            }

            val registerValues = executing.map {
                val currentCycle = cycle
                val currentRegister = register

                if (closedRange(currentRegister - 1, currentRegister + 1).contains((currentCycle - 1) % 40)) {
                    crtRow += "#"
                } else {
                    crtRow += "."
                }
                if (crtRow.size == 40) {
                    crtResult += crtRow.joinToString("")
                    crtRow.clear()
                }

                if (it.firstPart() == "addx") {
                    register += it.secondAsInt()
                }
                cycle++
                Pair(currentCycle, currentRegister)
            }
            executing.clear()

            registerValues
        })


    return crtResult.joinToString("/")


}


fun strength(state: Pair<Int, Int>): Int {
    return state.first * state.second
}
