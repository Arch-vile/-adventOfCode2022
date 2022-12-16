package day11

import aoc.utils.*

data class Monkey(
    val index: Int,
    var inspectionCount: Int,
    val worryLevels: MutableList<Int>,
    val op: (Int) -> Int,
    val divider: Int,
    val successTarget: Int,
    val failureTarget: Int
)

data class Move(val targetMonkey: Int, val worryLevel: Int)

fun main() {
    part1()
}

fun part1(): Int {
    val monkeys =
        readInput("day11-input.txt").windowed(6, 7).map { monkeyFromString(it) }.associateBy { it.index }

    repeat(20) {
        (0..monkeys.size - 1).map { index ->
            val monkey = monkeys[index]!!
            val moves = monkey.worryLevels
                .map { level ->
                    monkey.inspectionCount++
                    val newLevel = monkey.op.invoke(level) / 3
                    if (newLevel % monkey.divider == 0) {
                        Move(monkey.successTarget, newLevel)
                    } else {
                        Move(monkey.failureTarget, newLevel)
                    }
                }
            monkey.worryLevels.clear()

            moves.forEach {
                monkeys[it.targetMonkey]!!.worryLevels.add(it.worryLevel)
            }
        }
    }

    val result = monkeys.values.sortedBy { it.inspectionCount }.reversed()
        .take(2)
        .map { it.inspectionCount }
        .reduce{ acc, v -> acc*v}

    println(result)
    return result;
}

private fun monkeyFromString(it: List<String>) = Monkey(
    it[0].secondPart().replace(":", "").toInt(),
    0,
    it[1].toList().breakAfter { it == ":" }[1].joinToString("").replace(" ", "").split(",").map { it.toInt() }
        .toMutableList(),
    operationFrom(it[2]),
    testFromString(it[3]),
    it[4].split(" ").last().toInt(),
    it[5].split(" ").last().toInt(),
)

private fun testFromString(it: String): Int {
    if (it.contains("divisible by")) return it.split(" ").last().toInt()
    else throw Error("Unknown test $it")
}

fun operationFrom(input: String): (Int) -> Int {
    val split = input.split(" ")
    val leftOperand = split[5]
    val rightOperand = split[7]
    val operator = if (rightOperand == "old") "raise2" else split[6]

    return if (leftOperand == "old") {
        when (operator) {
            "+" -> { a -> a + rightOperand.toInt() }
            "*" -> { a -> a * rightOperand.toInt() }
            "raise2" -> { a -> a * a }
            else -> throw Error("unknown operator $operator")
        }
    } else {
        throw Error("unknown left operand $leftOperand")
    }
}

fun part2(): Int {
    return 1;
}
