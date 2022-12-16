package day8

import aoc.utils.Matrix
import aoc.utils.readInput
import aoc.utils.toList

data class Tree(val name: String, val hight: Int)

fun part1(): Int {
    val input = readInput("day8-input.txt")
        .map { it.toList().map { it.toInt() } }


    val treesO = input.mapIndexed { y, row -> row.mapIndexed { x, column ->
        Tree("$y,$x", column)  }
    }

   val trees = Matrix(treesO).values();

    val inLines = countInLines(trees)
//    println(inLines)

    val inRows = countInLines(Matrix(trees).rotateCW().values())
//    println(inRows)

    val combined = inLines.plus(inRows)
        .groupBy { it.tree.name }
        .map { it.value.map { it.score }.flatten() }
        .map { product(it) }
        .maxOf { it }
    println(combined)

    return  1
    // 1121 not correct
    // 1531 not correct
    // 1125 not correct
//    return combined.size + input.size*2 + (input[0].size-2)*2;
}

// TODO make better
fun product(it: List<Int>): Int {

    return it.reduce { acc, i ->  i*acc}



}


data class TreeScore(val tree: Tree, val score: List<Int>)

private fun countInLines(input: List<List<Tree>>): List<TreeScore> {
    val trees = mutableListOf<TreeScore>()

    for (y in input.indices) {
        for (x in input[0].indices) {

            val thisTree = input[y][x]
            val currentRow = input[y]
            val before = currentRow.subList(0, x)
            val after = currentRow.subList(x + 1, currentRow.size)

            val seenAfter = takeWhile(after, thisTree.hight)
            val seenBefore = takeWhile(before.reversed(), thisTree.hight)

            trees.add(TreeScore(thisTree, listOf(seenAfter.size,seenBefore.size)))

        }
    }
    return trees
}

// TODO make better
fun takeWhile(after: List<Tree>, hight: Int): List<Tree> {

    val result = mutableListOf<Tree>()
    var ended = false
    for(i in after.indices) {

        if (ended) {
            result.add(after[i])
            break
        }

        if (after[i].hight < hight ||i == after.size-1 )
            result.add(after[i])
        else
            ended = true

    }

return result
}

fun part2(): Int {
    return 1;
}
