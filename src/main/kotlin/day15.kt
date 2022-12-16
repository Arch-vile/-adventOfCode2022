package day15

import aoc.utils.Cursor
import aoc.utils.findInts
import aoc.utils.readInput
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.max

data class Sensor(val location: Cursor, val beacon: Cursor) {

    val distance: Int = location.distance(beacon)

    fun covers(point: Cursor) = location.distance(point) <= distance

    fun coveredRange(y: Int): ClosedRange<Int>? {
        val deltaY = abs(location.y - y)
        if (deltaY > distance) {
            return null
        }
        return (location.x - distance + deltaY)..(location.x + distance - deltaY)
    }
}

fun part1() {
    val rowUnderInspection = 2000000
    val sensors = readInput("day15-input.txt")
        .map { it.findInts() }
        .map { Sensor(Cursor(it[0], it[1]), Cursor(it[2], it[3])) }

    val minimumX = sensors.map { it.location.x - it.distance }.minOrNull()!!
    val maximumX = sensors.map { it.location.x + it.distance }.maxOrNull()!!

    val coveredPositions = (minimumX..maximumX).filter { x ->
        val coveredBySensor = sensors.firstOrNull { sensor -> sensor.covers(Cursor(x, rowUnderInspection)) }
        coveredBySensor != null
    }

    val beaconsOnRow = sensors.map { it.beacon }.filter { it.y == rowUnderInspection }.toSet().size
    val sensorsOnRow = sensors.map { it.location }.filter { it.y === rowUnderInspection }.toSet().size

    val ruledOutPositions = coveredPositions.size - beaconsOnRow - sensorsOnRow
    println(ruledOutPositions)
}

fun main() {

    val sensors = readInput("day15-input.txt")
        .map { it.findInts() }
        .map { Sensor(Cursor(it[0], it[1]), Cursor(it[2], it[3])) }

    (0..4000000).map { y ->
        println(y)

        val ranges = sensors.map {
            it.coveredRange(y)
        }.filterNotNull()

        val hasGaps = doesRowContainGaps(ranges)
        if (hasGaps) {
            (0..4000000).map { x ->
                val coveredBySensor = sensors.firstOrNull { sensor -> sensor.covers(Cursor(x, y)) }

                if (coveredBySensor == null) {
                    val result = x.toBigInteger() * (4000000).toBigInteger() + y.toBigInteger()
                    // 2026291660 too low
                    println(result)
                    TODO()
                }

            }
        }
    }

}

fun doesRowContainGaps(ranges: List<ClosedRange<Int>>): Boolean {
    // For the row to be fully covered there can be no gaps
    // this means ranges sorted by start must overlap
    val sorted = ranges.sortedBy { it.start }

    var combinedEnd = sorted.first().endInclusive
    for(range in sorted) {

        if(range.start > combinedEnd) {
            return true
        }
        combinedEnd = max(combinedEnd, range.endInclusive)
    }

    return false
}
