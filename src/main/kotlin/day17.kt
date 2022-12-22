package day17

import aoc.utils.*
import kotlin.text.toList

data class Shape(val blocks: List<Cursor>) {

    fun isFalling(stationary: List<Shape>): Boolean {
        val intersect = stationary.flatMap { it.blocks }.intersect(this.blocks.toSet())
        return intersect.isEmpty()
    }

    fun move(move: Cursor, stationary: MutableList<Shape>): Shape {
        val updated = this.copy(blocks = blocks.map { it.plus(move) })
        return if (!updated.isFalling(stationary) || updated.isOutOfBounds()) {
            this
        } else {
            updated
        }
    }

    fun blow(direction: Char, stationary: MutableList<Shape>): Shape {
        // Let's not move if not free
        if (!isFalling(stationary))
            return this

        val move = when (direction) {
            '>' -> Cursor(1, 0)
            '<' -> Cursor(-1, 0)
            else -> throw Error("unknown direction")
        }
        return this.move(move, stationary)
    }

    private fun isOutOfBounds(): Boolean {
        val maxX = blocks.maxOf { it.x }
        val minX = blocks.minOf { it.x }

        return minX < 0 || maxX > 6
    }
}


fun part1(): Long {
    return runSimulation(2022)
}

fun part2(): Long {
    return runSimulation(2022)
}

fun main() {

    val matrix = Matrix(10,10){a,b -> "d"}

    val timer = Timer(1000000000000)
    var i = 0L
    while(i < 1000000000000) {
       timer.processed = i+1
        val v  = matrix.get(Cursor((i % matrix.width()).toInt(),0))
        val t  = matrix.get(Cursor((i % matrix.width()).toInt(),(i%matrix.height()).toInt()))
        val p = t.cursor.plus(Cursor(1,2))
        matrix.rows()[(i%matrix.height()).toInt()].map { it.cursor.plus(Cursor(1,1)) }
        i+=1
    }


    runSimulation(100)
}

fun runSimulation(rocksToDrop: Long): Long {
    val gasDirection = readInput("day17-input.txt").take(1).flatMap { it.toList() }
    val stationary = mutableListOf(floor)

    var nextGasIndex = 0;
    var nextRockIndex = 0
    var rockCount: Long = 0
    val timer = Timer(rocksToDrop)

    while (rockCount < rocksToDrop) {
        rockCount += 1
        timer.processed = rockCount

        val highestPoint = highestPoint(stationary)

        val nextShape = rocks.getLooping(nextRockIndex)
        var current = nextShape.move(Cursor(0, highestPoint.y + 4), stationary)

        while (true) {

            val direction = gasDirection.getLooping(nextGasIndex)
            current = current.blow(direction, stationary)
            nextGasIndex += 1

            val previous = current
            current = current.move(DOWN, stationary)
            if (previous == current)
                break

        }
        stationary.add(current)
        if (stationary.size > 150)
            stationary.removeAt(0)

        nextRockIndex++
    }

    println(visualize(stationary))

    val highestPoint = highestPoint(stationary)
    return highestPoint.y.toLong()
}

private fun tryFindPattern(
    stationary: MutableList<Shape>,
    patternToMatch: Set<Cursor>?
) {
    var patternToMatch1 = patternToMatch
    if (stationary.size > 20) {
        if (patternToMatch1 == null) {
            patternToMatch1 = stationary.takeLast(20).flatMap { it.blocks }.toSet()
            patternToMatch1 = normalize(patternToMatch1)
        } else {
            var test = stationary.takeLast(20).flatMap { it.blocks }.toSet()
            test = normalize(test)
            if (test.containsAll(patternToMatch1) && test.size == patternToMatch1.size) {
                throw Error("found")
            }
        }
    }
}

fun normalize(cursors: Set<Cursor>): Set<Cursor> {
    val lowest = cursors.minByOrNull { it.y }!!.y
    return cursors.map { it.minus(Cursor(0, lowest)) }.toSet()
}

private fun visualize(stationary: List<Shape>, rock: Shape? = null) {
    val symbols = ('a'..'z').toList()
    val lowest = lowestPoint(stationary).y

    val offsettedShapes = stationary.map { it.copy(blocks = it.blocks.map { it.minus(Cursor(0, lowest)) }) }

    val height = highestPoint(offsettedShapes).y

    val matrix = Matrix(7, height + 10L) { a, b -> "." }
    offsettedShapes.forEach { shape ->
        val symbol = symbols.random().toString()
        shape.blocks.forEach {
            matrix.replace(it) { symbol }
        }
    }
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

fun lowestPoint(stationary: List<Shape>): Cursor {
    return stationary.flatMap { it.blocks }
        .minByOrNull { it.y }!!
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
