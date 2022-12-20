package day20

import aoc.utils.readInput
import kotlin.math.abs

fun main() {
    // 7030 too low
    part1().let { println(it) }
}


data class Holder(var left: Holder?, var right: Holder?,val value: Int, var moveRight: Int, val originalIndex: Int) {
    override fun toString(): String {
        return "$value"
    }
}

fun part1(): Int {
    // Keep in the list to preserve the original order
    val originalOrder = readInput("day20-input.txt")
        .mapIndexed { index, value -> Holder(null, null,value.toInt(), value.toInt(), index) }

    val count = originalOrder.size

    // Create links
    originalOrder.forEachIndexed { index, holder ->
        val left = if (index == 0) originalOrder.last() else originalOrder[index - 1]
        val right = if (index == count - 1) originalOrder[0] else originalOrder[index + 1]
        holder.left = left
        holder.right = right
    }

    originalOrder.forEach {

        if(it.value != 0) {

            // Link left and right of this instead, i.e. remove this from between
            it.left?.right =it.right
            it.right?.left = it.left

            // Find the new left neighbour
            val newLeft = move(it, it.value)

            // Place this between
            val oldRight = newLeft.right
            newLeft.right = it
            oldRight?.left = it
            it.left = newLeft
            it.right = oldRight
        }
    }

    val zero = originalOrder.firstOrNull { it.value == 0 }!!
    val thousandth1 = move(zero, 1000)
    val thousandth2 = move(zero, 2000)
    val thousandth3 = move(zero, 3000)

    println(thousandth1)
    println(thousandth2)
    println(thousandth3)

    return thousandth1.value + thousandth2.value + thousandth3.value
}

private fun printIt(originalOrder: List<Holder>) {
    var current = originalOrder[0]
    repeat(originalOrder.size) {
        print(current.value.toString()+", ")
        current = current.right!!
    }
    println()
}

fun move(it: Holder, amount: Int): Holder {
   var current = it
   repeat(abs(amount) + if(amount<0) 1 else 0) {
      current = if(amount<0) current.left!! else current.right!!
   }
    return current
}

fun part2(): Int {
    return 1;
}
