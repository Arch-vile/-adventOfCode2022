package day5

import utils.readInput

fun part1(): Int {

    val crates = mutableListOf(
        mutableListOf<String>(),
        mutableListOf<String>(),
        mutableListOf<String>(),
        mutableListOf<String>(),
        mutableListOf<String>(),
        mutableListOf<String>(),
        mutableListOf<String>(),
        mutableListOf<String>(),
        mutableListOf<String>()
    )

    readInput("input.txt")
        .take(8)
        .map { it.replace(" ", "") }
        .map { it.split(",").map { it.replace("]","").replace("[","") } }
        .forEach { letters -> letters.forEachIndexed{ index, letter -> if(letter !== "") crates[index].add(0,letter)  }}

    readInput("input.txt")
        .drop(10)
        .map { it.split(" ") }
        .map { makeMove(it[1].toInt(),it[3].toInt()-1,it[5].toInt()-1,crates) }

    crates
        .map { it.last() }
        .forEach{ print(it) }
    return 1;
}

fun makeMove(amount: Int, from: Int, to: Int, crates: MutableList<MutableList<String>>) {


    repeat(amount){ i ->
        val toMove = crates[from].last()
        crates[from] = crates[from].dropLast(1).toMutableList()
        crates[to] = crates[to].plus(toMove).toMutableList()
    }



}

fun part2(): Int {
    val foo = mutableListOf(1,2,3)
        foo.drop(1)
    val a  = foo.plus(5)
    println(a)
    return 1;
}
