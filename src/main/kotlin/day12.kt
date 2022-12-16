package day12

import aoc.utils.*
import aoc.utils.graphs.Node
import aoc.utils.graphs.shortestPath
import java.lang.Exception

data class Square(var height: Char, var isVisited: Boolean)

fun main() {
    part2()
}

fun part1(): Long {
    val map = Matrix(
        readInput("day12-input.txt").map { it.toCharArray().toList() }  )

    val start = map.find { it == 'S' }!!
    val end = map.find { it == 'E' }!!
    map.replace(start,'a')
    map.replace(end,'z')

    val graphMatrix = buildFromMatrix(map){ relative, from, to ->
        if(relative != Cursor(-1,-1)
            && relative != Cursor(-1,1)
            && relative != Cursor(1,-1)
            && relative != Cursor(1,1)
            && to <= from+1) {
           1
        } else null
    }

    val startNode = graphMatrix.get(start.cursor).value
    val endNode = graphMatrix.get(end.cursor).value
    return shortestPath(startNode, endNode)!!
}

fun part2(): Long {
    val map = Matrix(readInput("day12-input.txt")
        .map { it.toList().map { Node(Square(it[0], false)) } })
    val start = map.find { it.value.height == 'S' }!!
    val end = map.find { it.value.height == 'E' }!!

    start.value.value.height = 'a'
    end.value.value.height = 'z'
    buildGraph(map, start)

    val anyShortest = map.findAll { it.value.value.height == 'a' }
        .map {
            try {
                val foo = shortestPath(it.value, end.value)!!
                foo
            } catch (e: Exception) {
                // TODO shortest path should not error
                10000
            }
        }
        .minOf { it }

    return anyShortest
}

/**
 * Builds a graph form matrix with the idea that adjacent (also diagonal) entries can be connected.
 * Will create a node for each element in the matrix.
 * Will loop through the matrix and determined by the shouldLink predicate link nodes together.
 * For example given a matrix of:
 * "abc"
 * "def",
 * looking at "a" the shouldLink will be called with (notice the same elements in two ways):
 * (a,b),(a,e),(a,d),(b,a),(e,a),(d,a)
 *
 * As a node is created for each element in matrix, multiple isolated graphs could be formed.
 *
 * The shouldLink returns the distance between nodes or null if there should not be a link.
 * The relative position of the "to" node is given.
 */
fun <T> buildFromMatrix(matrix: Matrix<T>, distance: (relative: Cursor, from: T, to: T) -> Long?): Matrix<Node<T>> {
    val nodeMatrix = matrix.map { Node(it.value) }
    nodeMatrix.allInColumnOrder().forEach { thisNode ->
        val neighbours = getSurrounding(nodeMatrix,thisNode)

        neighbours.forEach { neighbour ->
            val distance = distance(neighbour.cursor.minus(thisNode.cursor),thisNode.value.value, neighbour.value.value)
            if(distance != null)
                thisNode.value.link(distance, neighbour.value)
        }
    }
    return nodeMatrix
}

private fun <T> getSurrounding(
    matrix: Matrix<Node<T>>,
    it: Entry<Node<T>>
) = matrix.getRelativeAt(
    it.cursor,
    listOf(
        // Top
        Cursor(-1, -1),
        Cursor(0, -1),
        Cursor(1, -1),

        // Same line
        Cursor(-1, 0),
        Cursor(1, 0),

        // Bottom
        Cursor(-1, 1),
        Cursor(0, 1),
        Cursor(1, 1),
    )
)

fun buildGraph(map: Matrix<Node<Square>>, current: Entry<Node<Square>>): Entry<Node<Square>> {
    current.value.value.isVisited = true

    val neighbours = map.getDirectNeighbours(current.cursor)

    val accessible = neighbours
        .filter { it.value.value.height <= current.value.value.height + 1 }

    current.value.link(1, accessible.map { it.value })

    accessible
        .filter { !it.value.value.isVisited }
        .forEach { buildGraph(map, it) }

    return current
}


