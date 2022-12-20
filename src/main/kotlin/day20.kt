package day20

import aoc.utils.readInput
import kotlin.math.abs

fun main() {


    // 7030 too low
    part2().let { println(it) }
}


data class Holder(var left: Holder?, var right: Holder?, var move: Long, var originalValue: Long, val originalIndex: Int) {
    override fun toString(): String {
        return "$move"
    }
}

fun part2(): Long {
    // Keep in the list to preserve the original order
    val originalOrder = readInput("day20-input.txt")
        .mapIndexed { index, value -> Holder(null, null, 0, value.toLong() * 1, index) }

    val count = originalOrder.size

    // Normalize
    originalOrder.forEach {
//        it.move = it.originalValue % count
        it.move = it.originalValue
    }

    // Create links
    originalOrder.forEachIndexed { index, holder ->
        val left = if (index == 0) originalOrder.last() else originalOrder[index - 1]
        val right = if (index == count - 1) originalOrder[0] else originalOrder[index + 1]
        holder.left = left
        holder.right = right
    }

    repeat(1) {
        originalOrder.forEach {

            if (it.move != 0L) {

                // Link left and right of this instead, i.e. remove this from between
                it.left?.right = it.right
                it.right?.left = it.left

                // Find the new left neighbour
                val newLeft = move(it, it.move)

                // Place this between
                val oldRight = newLeft.right
                newLeft.right = it
                oldRight?.left = it
                it.left = newLeft
                it.right = oldRight
            }
//            printIt(originalOrder)
        }
    }

    val zero = originalOrder.firstOrNull { it.move == 0L }!!
    val thousandth1 = move(zero, 1000)
    val thousandth2 = move(zero, 2000)
    val thousandth3 = move(zero, 3000)

    println(thousandth1.originalValue)
    println(thousandth2.originalValue)
    println(thousandth3.originalValue)

    return thousandth1.originalValue + thousandth2.originalValue + thousandth3.originalValue
}

private fun printIt(originalOrder: List<Holder>) {
    var current = originalOrder[0]
    repeat(originalOrder.size) {
        print(current.originalValue.toString() + ", ")
        current = current.right!!
    }
    println()
}

fun move(it: Holder, amount: Long): Holder {
    var current = it
    repeat(abs(amount.toInt()) + if (amount < 0) 1 else 0) {
        current = if (amount < 0) current.left!! else current.right!!
    }
    return current
}

