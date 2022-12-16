package day5

import utils.Matrix
import utils.readInput

fun part1(): String {
    return solve(::makeMove)
}

fun part2(): String {
    return solve(::makeMove2)
}


fun solve(mover: (Int, Int, Int, MutableList<MutableList<String>>) -> Unit): String {

    val crates = createCrates()

    readInput("day5-input.txt")
        .drop(10)
        .map { it.split(" ") }
        .forEach { mover(it[1].toInt(), it[3].toInt() - 1, it[5].toInt() - 1, crates) }

    return crates.joinToString("") { it.last() }

}

private fun createCrates(): MutableList<MutableList<String>> {
    val read = readInput("day5-input.txt")
        .take(8)
        .map { it.windowed(3,4) }
        .map { it.map {  it.replace("[", "").replace("]","").replace(" ","")}  }

    return Matrix(read).rotateCW().values().map { it.filter { it != "" }.toMutableList() }.toMutableList()
}

fun makeMove2(amount: Int, from: Int, to: Int, crates: MutableList<MutableList<String>>) {
    val toMove = crates[from].takeLast(amount)
    crates[from] = crates[from].dropLast(amount).toMutableList()
    crates[to] = crates[to].plus(toMove).toMutableList()
}

fun makeMove(amount: Int, from: Int, to: Int, crates: MutableList<MutableList<String>>) {
    repeat(amount) {
        val toMove = crates[from].last()
        crates[from] = crates[from].dropLast(1).toMutableList()
        crates[to] = crates[to].plus(toMove).toMutableList()
    }
}

