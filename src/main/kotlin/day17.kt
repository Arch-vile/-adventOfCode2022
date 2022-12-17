package day17

import aoc.utils.*
import kotlin.text.toList

data class Shape(val blocks: List<Cursor>) {

    fun isFalling(stationary: List<Shape>): Boolean {
        val intersect = stationary.flatMap { it.blocks }.intersect(this.blocks.toSet())
        return intersect.isEmpty()
    }

    fun move(move: Cursor, stationary: MutableList<Shape>): Shape {
        val updated = this.copy(blocks = blocks.map { it.move(move) })
        return if(!updated.isFalling(stationary) || updated.isOutOfBounds()) {
            this
        } else {
            updated
        }
    }

    fun blow(direction: Char, stationary: MutableList<Shape>): Shape {
        // Let's not move if not free
        if(!isFalling(stationary))
            return this

        val move = when (direction) {
            '>' -> Cursor(1, 0)
            '<' -> Cursor(-1, 0)
            else -> throw Error("unknown direction")
        }
        return this.move(move,stationary)
    }

    private fun isOutOfBounds(): Boolean {
        val maxX = blocks.maxOf { it.x }
        val minX = blocks.minOf { it.x }

       return minX < 0 || maxX > 6
    }
}

fun part1(): Int {
    val gasDirection = readInput("day17-input.txt").take(1).flatMap { it.toList() }
    val stationary = mutableListOf(floor)

    var nextGasIndex = 0;
    var nextRockIndex = 0
    repeat(2022) {
        val highestPoint = highestPoint(stationary)

        var current = rocks.getLooping(nextRockIndex).move(Cursor(0, highestPoint.y + 4),stationary)

        while (true) {
            val direction = gasDirection.getLooping(nextGasIndex)
            current = current.blow(direction, stationary)
            nextGasIndex+=1

            val previous = current
            current = current.move(DOWN,stationary)
            if(previous == current)
                break

        }
        stationary.add(current)

        nextRockIndex++
    }

    val highestPoint = highestPoint(stationary)
    return highestPoint.y
}

private fun visualize(stationary: MutableList<Shape>, rock: Shape? = null) {
    val matrix = Matrix(7, 20) { a, b -> "." }
    stationary.flatMap { it.blocks }
        .forEach { matrix.replace(it) { "#" } }
    rock?.let {
        it.blocks.forEach { matrix.replace(it) { "@" } }
    }
    println(matrix.flipHorizontal().visualize(""))
    println("------------------------------")
}

fun highestPoint(stationary: List<Shape>): Cursor {
    return stationary.flatMap { it.blocks }
        .maxByOrNull { it.y }!!
}

fun part2(): Int {
    return 1;
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
    )
)
val rocks = listOf(hLine, cross, lBlock, vLine, square)
val DOWN = Cursor(0, -1)
