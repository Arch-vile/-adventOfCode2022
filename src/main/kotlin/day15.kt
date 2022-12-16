package day15

import aoc.utils.Cursor
import aoc.utils.findInts
import aoc.utils.readInput

data class Sensor(val location: Cursor, val beacon: Cursor) {
    fun distance() = location.distance(beacon)
    fun covers(point: Cursor) = location.distance(point) <= distance()
}

fun main() {
    val sensors = readInput("test.txt")
        .map { it.findInts() }
        .map { Sensor(Cursor(it[0],it[1]), Cursor(it[2], it[3])) }

    val minimumX = sensors.map { it.location.x-it.distance() }.minOrNull()!!
    val maximumX = sensors.map { it.location.x+it.distance() }.maxOrNull()!!

    val uncoveredPositions = (minimumX..maximumX).filter{ x ->
        val coveredBySensor = sensors.firstOrNull { sensor -> sensor.covers(Cursor(x,10)) }
        coveredBySensor == null
    }
    println(uncoveredPositions)
}

fun part2(): Int {
    return 1;
}
