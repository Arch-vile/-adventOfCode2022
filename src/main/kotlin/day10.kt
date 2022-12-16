package day10

import aoc.utils.closedRange
import aoc.utils.firstPart
import aoc.utils.readInput
import aoc.utils.secondAsInt

fun part2(): String {

    var cycle = 1
    var register = 1
    var executing = mutableListOf<String>()
    var crtRow = mutableListOf<String>()

    var crtResult = mutableListOf<String>()


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

                if( closedRange(currentRegister-1,currentRegister+1).contains((currentCycle-1)%40) ) {
                   crtRow += "#"
                } else {
                    crtRow += "."
                }
                if(crtRow.size == 40){
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

fun part1(): Int {
    return 1;
}

fun strength(state: Pair<Int,Int>): Int {
    return state.first * state.second
}
