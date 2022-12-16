package day12

import aoc.utils.Entry
import aoc.utils.Matrix
import aoc.utils.graphs.Node
import aoc.utils.graphs.allNodes
import aoc.utils.graphs.shortestPath
import aoc.utils.readInput
import aoc.utils.toList
import java.awt.geom.GeneralPath

data class Square(var height: Char, var isVisited: Boolean)

fun main() {
    part1()
}

fun part1(): Int {
    val map = Matrix(readInput("day12-input.txt")
        .map { it.toList().map { Node(Square(it[0], false)) } })
    val start = map.find{ it.value.height == 'S'}!!
    val end = map.find{ it.value.height == 'E'}!!

    start.value.value.height = 'a'
    end.value.value.height = 'z'

    val graph = buildGraph(map, start)

    val foo = shortestPath(start.value, end.value)
   println(foo)
    return 1;
}

fun buildGraph(map: Matrix<Node<Square>>, current: Entry<Node<Square>>): Entry<Node<Square>> {
    current.value.value.isVisited = true

    val neighbours = map.getDirectNeighbours(current.cursor)

    val accessible = neighbours
        .filter { it.value.value.height <= current.value.value.height + 1 }

    current.value.link(1, accessible.map { it.value })

    accessible
        .filter { !it.value.value.isVisited }
        .forEach { buildGraph(map,it ) }

    return current
}


fun part2(): Int {
    return 1;
}
