package day16

import aoc.utils.findInts
import aoc.utils.graphs.Node
import aoc.utils.readInput
import aoc.utils.splitOn

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
}



fun part1(): Int {
    readInput("dayWIP-input.txt")
    return 1;
}

fun part2(): Int {
    return 1;
}
