package day21

import aoc.utils.findFirstInt
import aoc.utils.readInput

fun main() {
    part1().let { println(it) }
}

data class Monkey(
    val name: String,
    var operation: Char?,
    var left: Monkey?,
    var right: Monkey?,
    val leftPlaceHolder: String?,
    val rightPlaceHolder: String?,
    var value: Long?
) {

    override fun toString(): String {
        return "Monkey[$name]"
    }

    fun valued(): Long {
        if(value != null)
            return value!!

        return when (operation) {
            '+' -> left!!.valued() + right!!.valued()
            '-' -> left!!.valued() - right!!.valued()
            '*' -> left!!.valued() * right!!.valued()
            '/' -> left!!.valued() / right!!.valued()
            else -> throw Error("unhandled $operation")
        }

    }
}

fun part1(): Long {
    val monkeys = readInput("day21-input.txt")
        .map {
            val split = it.split(" ")
            if (split.size > 2) {
                Monkey(
                    it.split(":")[0],
                    split[2][0],
                    null,
                    null,
                    split[1],
                    split[3],
                    null
                )
            } else {
                Monkey(
                    it.split(":")[0],
                    null,
                    null,
                    null,
                    null,
                    null,
                    it.findFirstInt().toLong()
                )
            }
        }.map { it.name to it }.toMap()

    // Link monkeys
    monkeys.values.forEach { monkey ->
        if(monkey.leftPlaceHolder != null) {
            monkey.left = monkeys[monkey.leftPlaceHolder]
            monkey.right = monkeys[monkey.rightPlaceHolder]
        }
    }

    val root = monkeys["root"]!!



    return root.valued()
}

fun part2(): Int {
    return 1;
}
