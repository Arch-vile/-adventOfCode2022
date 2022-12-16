package day16

import aoc.utils.findInts
import aoc.utils.graphs.Node
import aoc.utils.graphs.allNodes
import aoc.utils.graphs.shortestPath
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

    val routes: List<List<Node<Valve>>> = allRoutes(allNodes(start).size, setOf(start.value.name), listOf(start))

//    routes.map { it.map { it.value.name } }
//        .forEach(::println)

    routes.map { it.map { it.value.name } }
        .forEach(::println)

    println("done")

}

fun allRoutes(
    nodeCount: Int,
    visited: Set<String>,
    routeSoFar: List<Node<Valve>>
): List<List<Node<Valve>>> {
    val current = routeSoFar.last()

    // We have visited all nodes, no where to expand
    if (visited.size == nodeCount) {
        return listOf( routeSoFar);
    }

    // Can we get to new place from this node?
    val neighboursToVisit = current.edges.filter { !visited.contains(it.target.value.name) }
    if (neighboursToVisit.isNotEmpty()) {
        return neighboursToVisit.flatMap { allRoutes(nodeCount, visited.plus(it.target.value.name), routeSoFar.plus(it.target)) }
    }

    // Unvisited nodes remain, but cannot directly access any from current, kind of dead end that is
    // Let's backtrack until we find a node that connects to an unvisited node
    val reverseBackTo = routeSoFar.reversed().first {
        val connectsTo = it.edges.map { it.target.value.name }
        val unvisited = connectsTo.firstOrNull { !visited.contains(it) }
        unvisited != null
    }
    // FIXME: It could be that the one we should start from is not the first one we found backtracking?
    val pathToReverse = shortestPath(current,reverseBackTo)
    return allRoutes(nodeCount, visited, routeSoFar + pathToReverse )
}

fun part1(): Int {
    readInput("dayWIP-input.txt")
    return 1;
}

fun part2(): Int {
    return 1;
}
