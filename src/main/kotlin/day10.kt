package day10

import aoc.utils.firstPart
import aoc.utils.readInput
import aoc.utils.secondAsInt

fun part1(): Int {

    var cycle = 1
    var register = 1
    var executing = mutableListOf<String>()
    val result = listOf(Pair(0,register)).plus(readInput("day10-input.txt")
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
                if (it.firstPart() == "addx") {
                    register += it.secondAsInt()
                }
                cycle++
                Pair(currentCycle, currentRegister)
            }
            executing.clear()

            registerValues
        })

    return listOf(result[20],result[60],result[100],result[140],result[180],result[220])
        .sumOf { strength(it) }

}

fun part2(): Int {
    return 1;
}

fun strength(state: Pair<Int,Int>): Int {
    return state.first * state.second
}
