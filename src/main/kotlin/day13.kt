package day13

import aoc.utils.findFirstInt
import aoc.utils.indexesOf
import aoc.utils.takeUntilMatch
import aoc.utils.toList

fun main() {

//    """\[[^\]]*\]""".toRegex().matches("[1,2,3]").let { println(it) }
//    """\[[^\]]*\]""".toRegex().matches("[a,[]]").let { println(it) }

    part1()

}

fun part1(): Int {
    val foo = readList("[[1,2],[3]]")
    return 1;
}

data class Listing(val children: List<Listing>?, val value: Int?)

fun readList(from: String): Listing {
    TODO()

    val trimmed = from.replace("""\s+""".toRegex(), "")

    // Simple list remains, this is either [],[1] or [1,2,...]
    if ("""\[[^\]]*\]""".toRegex().matches(trimmed)) {
        val numbers = trimmed.substring(1, trimmed.length - 1).split(",").map { it.toInt() }
        val simpleListings = numbers.map { Listing(null, it) }
        return Listing(simpleListings, null)
    }

    // List that must have at least one internal list in it
    // [1,[....],...] or [[]]
    val parts = mutableListOf<Listing>()
    var remains = trimmed.substring(1, trimmed.length - 1)
    while (remains != "") {
        // Next up a number
        if ("""\d+""".toRegex().matches(remains)) {
            val number = remains.findFirstInt().toString()
            remains = remains.removePrefix("$number")
            parts.add(Listing(null, number.toInt()))
        }

        // Next up a list
        if (remains.startsWith('[')) {
            // TODO: Could write a helper for this thing
            val listContent = remains.takeUntilMatch {
                val countOpening = it.toList().indexesOf { it == "[" }.size
                val countClosing = it.toList().indexesOf { it == "]" }.size
                countOpening != 0 && countOpening == countClosing
            }
            remains = listContent.second
            parts.add(readList(listContent.first))
        }


    }


}

fun part2(): Int {
    return 1;
}

