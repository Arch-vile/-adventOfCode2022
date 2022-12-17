package day17

import aoc.utils.Cursor
import aoc.utils.Matrix
import aoc.utils.readInput

data class Shape(val blocks: List<Cursor>, var lastMove: Cursor = Cursor(0,0)) {


    fun isFalling(stationary: List<Shape>): Boolean {
        val intersect = stationary.flatMap { it.blocks }.intersect(this.blocks.toSet())
        return intersect.isEmpty()
    }

    fun offset(move: Cursor): Shape {
        lastMove = move
       return this.copy( blocks = blocks.map { it.move(move) } )
    }

    fun revertLast(): Shape {
       return offset(Cursor(lastMove.x*-1,lastMove.y*-1))
    }
}

val hLine = Shape(
    listOf(
        Cursor(2, 0),
        Cursor(3, 0),
        Cursor(4, 0),
        Cursor(5, 0),
    )
)

val vLine = Shape(
    listOf(
        Cursor(2, 0),
        Cursor(2, 1),
        Cursor(2, 2),
        Cursor(2, 3),
    )
)

val cross = Shape(
    listOf(
        Cursor(2, 1),
        Cursor(3, 1),
        Cursor(4, 1),
        Cursor(3, 0),
        Cursor(3, 2),
    )
)

val lBlock = Shape(
    listOf(
        Cursor(2, 0),
        Cursor(3, 0),
        Cursor(4, 0),
        Cursor(4, 1),
        Cursor(4, 2),
    )
)

val square = Shape(
    listOf(
        Cursor(2, 0),
        Cursor(3, 0),
        Cursor(2, 1),
        Cursor(3, 1),
    )
)

val floor = Shape(
    listOf(
        Cursor(0, 0),
        Cursor(1, 0),
        Cursor(2, 0),
        Cursor(3, 0),
        Cursor(4, 0),
        Cursor(5, 0),
        Cursor(6, 0),
        Cursor(7, 0),
    )
)
val rocks = listOf(hLine, cross, lBlock, vLine, square)
val DOWN = Cursor(0,-1)

fun main() {
    val gasDirection = readInput("test.txt").toList()
    val stationary = mutableListOf(floor)

    val highestPoint = highestPoint(stationary)
    var rock = cross.offset(Cursor(0,highestPoint.y+3))
    while (rock.isFalling(stationary)) {
        rock=rock.offset(DOWN)
    }
    rock = rock.revertLast()
    stationary.add(rock)

    val matrix = Matrix(10,20) { a,b -> "."}
    stationary.flatMap { it.blocks }
        .forEach{
            matrix.replace(it) { "#"}
        }
    println(matrix.flipHorizontal().visualize(""))
}

fun highestPoint(stationary: List<Shape>): Cursor {
    return stationary.flatMap { it.blocks }
        .maxByOrNull { it.y }!!
}

fun part1(): Int {
    readInput("dayWIP-input.txt")
    return 1;
}

fun part2(): Int {
    return 1;
}
