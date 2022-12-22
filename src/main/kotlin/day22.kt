package day22

import aoc.utils.*


val UP = Cursor(0, -1)
val DOWN = Cursor(0, 1)
val LEFT = Cursor(-1, 0)
val RIGHT = Cursor(1, 0)
val ROTATIONS = listOf(UP, RIGHT, DOWN, LEFT)

data class Actor(val location: Cursor, val direction: Cursor, val map: Matrix<Char>) {
    fun turn(direction: Char): Actor {
        val newDirection = when (direction) {
            'R' -> ROTATIONS.getLooping(ROTATIONS.indexOf(this.direction) + 1)
            'L' -> ROTATIONS.getLooping(ROTATIONS.indexOf(this.direction) - 1)
            '?' -> this.direction
            else -> throw Error("unknown direction $direction")
        }

        return copy(direction = newDirection)
    }


    // TODO: Optimize the uncharted territory travel?
    fun move(move: Int): Actor {
        var validLocation = location
        var newLocation = location
        repeat(move) {
            newLocation = map.putInBounds(newLocation.plus(direction))
            // uncharted territory, let's eat those away
            while (map.get(newLocation).value == ' ') {
                newLocation = map.putInBounds(newLocation.plus(direction))
            }
            // Hit the wall, step back
            if (map.get(newLocation).value == '#') {
                return copy(location = validLocation)
            } else {
                validLocation = newLocation
            }

        }
        return copy(location = validLocation)
    }

}

fun main() {

    part1().let { println(it) }
}

fun part1(): Int {
    val map = readMap(input())
    val directions = directions(input())
    val startPosition = findStart(map)
    var actor = Actor(startPosition, RIGHT, map)

    directions.forEach { direction ->
        if (direction.turn != '?') {
            actor = actor.turn(direction.turn)
        } else {
            actor = actor.move(direction.move)
        }


//        val temp = map.get(actor.location)
//        map.replace(actor.location) { '@' }
//        println(map.visualize(""))
//        println("*******************************")
//        map.replace(actor.location) { temp.value }
    }

//    println(actor.location)
//    println(actor.direction)

//    map.replace(actor.location) { '@' }
//
//    println(map.visualize(""))

    return (actor.location.y+1)*1000+ (actor.location.x+1)*4 + when (actor.direction) {
       RIGHT -> 0
       DOWN -> 1
       LEFT -> 2
       UP -> 3
       else -> throw Error()
    }
}

fun findStart(map: Matrix<Char>): Cursor {
    map.rows()[0].forEachIndexed { index, entry ->
        if (entry.value == ' ')
            return map.get(index, 0).cursor
    }
    throw Error("Could not find start")
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
