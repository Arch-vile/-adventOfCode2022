package day22

import aoc.utils.*


val UP = Cursor(0, -1)
val DOWN = Cursor(0, 1)
val LEFT = Cursor(-1, 0)
val RIGHT = Cursor(1, 0)
val ROTATIONS = listOf(UP, RIGHT, DOWN, LEFT)

data class Actor(val location: Block, val direction: Block) {
    fun turn(direction: Char): Actor {
        val currentDirectionIndex = location.neighbours.indexOf(this.direction)
        val newDirection = when (direction) {
            'R' -> location.neighbours.getLooping(currentDirectionIndex+1)
            'L' -> location.neighbours.getLooping(currentDirectionIndex-1)
            '?' -> this.direction
            else -> throw Error("unknown direction $direction")
        }

        return copy(direction = newDirection)
    }

    fun move(movesLeft: Int): Actor {
        if(movesLeft == 0) {
            return this
        }

        val newLocation = this.direction
        val newDirection = newLocation.oppositeOf(this.location)

        if(newLocation.type == '#') {
            return this
        } else {
            return copy(location =newLocation, direction = newDirection).move(movesLeft-1)
        }
    }

}

fun main() {

    part2().let { println(it) }
}

data class Block(
    var type: Char,
    var location: Cursor,
    var neighbours: List<Block>
) {
    override fun toString(): String {
        return type.toString()
    }

    // As the neighbours are in clockwise order, we get the opposite by indexing +2
    fun oppositeOf(of: Block): Block {
        val index = neighbours.indexOf(of)
        return neighbours.getLooping(index+2)
    }
}

fun part2(): Int {
//    val map = readMap(input()).map {
//        Block(it.value, it.cursor, mutableListOf())
//    }
//
//    map.findAll { it.value.type != ' ' }
//        .forEach {
//            it.value.left = getNextLooping(it.cursor, LEFT, map)
//        }
//
//    return solve(map)
    return 1
}

fun part1(): Int {
    val map = readMap(input()).map {
        Block(it.value, it.cursor, listOf())
    }

    map.findAll { it.value.type != ' ' }
        .forEach {
            it.value.neighbours =
                    // Order must be in clockwise, and for end scoring same order as scores given
                listOf(
                    getNextLooping(it.cursor, RIGHT, map),
                    getNextLooping(it.cursor, DOWN, map),
                    getNextLooping(it.cursor, LEFT, map),
                    getNextLooping(it.cursor, UP, map),
                )
        }

    return solve(map)
}

private fun solve(map: Matrix<Block>): Int {
    val directions = directions(input())
    val startPosition = findStart(map)
    var actor = Actor(startPosition, startPosition.neighbours[0])

    directions.forEachIndexed { index, direction ->
        println(index)
        val temp = map.get(actor.location.location).value.type
        map.get(actor.location.location).value.type = '@'
        println(map.visualize(""))
        println("*******************************")
        map.get(actor.location.location).value.type = temp

        if (direction.turn != '?') {
            actor = actor.turn(direction.turn)
        } else {
            actor = actor.move(direction.move)
        }



    }

//    println(actor.location)
//    println(actor.direction)

//    map.replace(actor.location) { '@' }
//
//    println(map.visualize(""))

    val actorSpot = map.find { it == actor.location }!!

    println(actorSpot)

    return (actorSpot.cursor.y + 1) * 1000 + (actorSpot.cursor.x + 1) * 4
    + actor.location.neighbours.indexOf(actor.direction)
}

fun getNextLooping(cursor: Cursor, direction: Cursor, map: Matrix<Block>): Block {
    var newLocation = map.putInBounds(cursor.plus(direction))
    while (map.get(newLocation).value.type == ' ') {
        newLocation = map.putInBounds(newLocation.plus(direction))
    }
    return map.get(newLocation).value
}

fun findStart(map: Matrix<Block>): Block {
    return map.rows()[0].firstOrNull { it.value.type == '.' }!!.value
}

data class Direction(val move: Int, val turn: Char)

fun directions(input: List<String>): List<Direction> {
    return """R|L|\d+""".toRegex().findAll(input.last())
        .map { it.value }
        .map {
            if (it.isInt()) {
                Direction(it.toInt(), '?')
            } else {
                if (it.length != 1) throw Error("Expected single char")
                Direction(0, it[0])
            }
        }.toList()
}


fun readMap(input: List<String>): Matrix<Char> {
    val mapRows = input.takeWhile { it != "" }.map { it.toCharArray().toList() }
    return Matrix(mapRows) { x, y -> ' ' }
}

private fun input() = readInput("day22-input.txt")
