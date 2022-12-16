package day16

import aoc.utils.findInts
import aoc.utils.graphs.Node
import aoc.utils.readInput
import aoc.utils.splitOn
import kotlin.math.max

data class Valve(val name: String, val flow: Int)


fun main() {
    val valves = readInput("test.txt")
        .map { it.split(" ").let { it[1] to it[4].findInts()[0] } }
        .map { Node(Valve(it.first, it.second)) }

    readInput("test.txt")
        .map { it.replace("valve ", "valves ") }
        .map {
            it.split(" ").let {
                val connected = it.splitOn { it == "valves" }[1].map { it.replace(",", "") }
                it[1] to connected
            }
        }
        .forEach { connection ->
            val valve = valves.firstOrNull { it.value.name == connection.first }!!
            connection.second.forEach { target ->
                val targetValve = valves.firstOrNull { it.value.name == target }!!
                valve.biLink(1, targetValve)
            }
        }

    val start = valves.firstOrNull { it.value.name == "AA" }!!

    println( venture(30, start, start, listOf(), 0))
}

fun venture(timeLeft: Int, current: Node<Valve>, from: Node<Valve>, valvesOpened: List<Node<Valve>>, pressureReleased: Int): Int {

   val accumPressure = pressureReleased + pressureChange(valvesOpened)

    val timeRemaining = timeLeft-1
    if (timeRemaining == 0)
        return accumPressure

    // Never go back to where we just came from (if we opened current == from)
    val connections = current.edges.map { it.target }.filter { it != from  }

    // Can't move anywhere and current was already opened, we can just stand still?
    if(connections.isEmpty() && valvesOpened.contains(current)) {
       return venture(timeRemaining, current, from, valvesOpened, accumPressure)
    }

    val moveScore =
        connections.map {
            venture(timeRemaining, it, current, valvesOpened, accumPressure )
        }.maxOfOrNull{ it }

    if(!valvesOpened.contains(current)) {
        return max(moveScore ?: 0,
           venture(timeRemaining,current,from,valvesOpened.plus(current),accumPressure)
            )
    }

    if(moveScore==null)
        println()
    return moveScore!!
}

fun pressureChange(valvesOpened: List<Node<Valve>>): Int {
    return valvesOpened.sumOf { it.value.flow }
}

fun part1(): Int {
    readInput("dayWIP-input.txt")
    return 1;
}

fun part2(): Int {
    return 1;
}
