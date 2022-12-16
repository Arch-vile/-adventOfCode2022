package day11

import aoc.utils.*
import java.math.BigInteger

data class Monkey(
    val index: Int,
    var inspectionCount: Int,
    val worryLevels: MutableList<BigInteger>,
    val op: (BigInteger) -> BigInteger,
    val divider: Int,
    val successTarget: Int,
    val failureTarget: Int
)

data class Move(val targetMonkey: Int, val worryLevel: BigInteger)

fun part1(): String {
    return solve(20, 3)
}

fun part2(): String {
    return solve(10000)
}

fun solve(rounds: Int, divider: Int? = null): String {
    val monkeys =
        readInput("day11-input.txt")
            .windowed(6, 7)
            .map { monkeyFromString(it) }
            .associateBy { it.index }

    val rootDivider = monkeys.values
        .map { it.divider.toBigInteger() }
        .reduce { acc, v -> acc * v }

    repeat(rounds) {
        (0..monkeys.size - 1).map { index ->
            val monkey = monkeys[index]!!
            val moves = monkey.worryLevels
                .map { level ->
                    monkey.inspectionCount++
                    var newLevel = monkey.op.invoke(level)
                    if (divider != null)
                        newLevel = newLevel.divide(divider.toBigInteger())
                    else if (newLevel > rootDivider)
                        newLevel = newLevel.mod(rootDivider)

                    if (newLevel.mod(monkey.divider.toBigInteger()).equals(0.toBigInteger())) {
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
        .map { it.inspectionCount.toBigInteger() }
        .reduce { acc, v -> acc * v }
    return result.toString();
}

private fun monkeyFromString(it: List<String>) = Monkey(
    it[0].findFirstInt(),
    0,
    it[1].findInts().map { it.toBigInteger() }.toMutableList(),
    operationFrom(it[2]),
    testFromString(it[3]),
    it[4].findFirstInt(),
    it[5].findFirstInt(),
)

private fun testFromString(it: String): Int {
    if (it.contains("divisible by")) return it.findFirstInt()
    else throw Error("Unknown test $it")
}

fun operationFrom(input: String): (BigInteger) -> BigInteger {
    val split = input.split(" ")
    val leftOperand = split[5]
    val rightOperand = split[7]
    val operator = if (rightOperand == "old") "raise2" else split[6]

    return if (leftOperand == "old") {
        when (operator) {
            "+" -> { a -> a + rightOperand.toBigInteger() }
            "*" -> { a -> a * rightOperand.toBigInteger() }
            "raise2" -> { a -> a * a }
            else -> throw Error("unknown operator $operator")
        }
    } else {
        throw Error("unknown left operand $leftOperand")
    }
}

