package day22

import aoc.utils.*


val UP = Cursor(0, -1)
val DOWN = Cursor(0, 1)
val LEFT = Cursor(-1, 0)
val RIGHT = Cursor(1, 0)
val ROTATIONS = listOf(UP, RIGHT, DOWN, LEFT)

data class Actor(val location: Block, val direction: Cursor) {
    fun turn(direction: Char): Actor {
        val newDirection = when (direction) {
            'R' -> ROTATIONS.getLooping(ROTATIONS.indexOf(this.direction) + 1)
            'L' -> ROTATIONS.getLooping(ROTATIONS.indexOf(this.direction) - 1)
            '?' -> this.direction
            else -> throw Error("unknown direction $direction")
        }

        return copy(direction = newDirection)
    }

    fun move(move: Int): Actor {
        var movesLeft = move
        var newLocation = location
        var prevLocation = location
        while(movesLeft != 0) {
            movesLeft--

            prevLocation = newLocation
            if (direction == UP)
                newLocation = newLocation.up!!
            if (direction == DOWN)
                newLocation = newLocation.down!!
            if (direction == LEFT)
                newLocation = newLocation.left!!
            if (direction == RIGHT)
                newLocation = newLocation.right!!

            if(newLocation.type == '#'){
                return copy(location = prevLocation)
            }
        }

        return copy(location = newLocation)
    }

}

fun main() {

    part1().let { println(it) }
}

data class Block(var type: Char, var location: Cursor, var left: Block?, var right: Block?, var up: Block?, var down: Block?) {
    override fun toString(): String {
        return type.toString()
    }
}

fun part1(): Int {
    val map = readMap(input()).map {
        Block(it.value, it.cursor, null, null, null, null)
    }

    map.findAll { it.value.type != ' ' }
        .forEach {
            it.value.left = getNextLooping(it.cursor, LEFT, map)
            it.value.right = getNextLooping(it.cursor, RIGHT, map)
            it.value.up = getNextLooping(it.cursor, UP, map)
            it.value.down = getNextLooping(it.cursor, DOWN, map)
        }

    return solve(map)
}

private fun solve(map: Matrix<Block>): Int {
    val directions = directions(input())
    val startPosition = findStart(map)
    var actor = Actor(startPosition, RIGHT)

    directions.forEachIndexed { index, direction ->
        if (direction.turn != '?') {
            actor = actor.turn(direction.turn)
        } else {
            actor = actor.move(direction.move)
        }


//        println(index)
//        val temp = map.get(actor.location.location).value.type
//        map.get(actor.location.location).value.type = '@'
//        println(map.visualize(""))
//        println("*******************************")
//        map.get(actor.location.location).value.type = temp
    }

//    println(actor.location)
//    println(actor.direction)

//    map.replace(actor.location) { '@' }
//
//    println(map.visualize(""))

    val actorSpot = map.find { it == actor.location }!!

    return (actorSpot.cursor.y + 1) * 1000 + (actorSpot.cursor.x + 1) * 4 + when (actor.direction) {
        RIGHT -> 0
        DOWN -> 1
        LEFT -> 2
        UP -> 3
        else -> throw Error()
    }
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

fun part2(): Int {
    return 1;
}

private fun input() = readInput("day22-input.txt")
