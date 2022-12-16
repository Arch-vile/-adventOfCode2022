package day13

import aoc.utils.*

fun part2() {
    val result = readInput("day13-input.txt")
        .plus("[[2]]")
        .plus("[[6]]")
        .filter { it != "" }
        .map { readList(it) }
        .sortedWith { a, b -> compareList(a, b) }
        .reversed()
        .mapIndexed { index, listing ->
            index + 1 to listing
        }
        .filter {
            isNestedListWithOneNumber(it.second, 2) ||
                    isNestedListWithOneNumber(it.second, 6)
        }
        .map { it.first }
        .reduce { acc, pair -> acc * pair }

    println(result)
}

fun isNestedListWithOneNumber(list: Listing, number: Int): Boolean {
    return list.children?.size == 1 &&
            list.children!![0].children?.size == 1 &&
            list.children!![0].children!![0].value == number
}

fun part1() {
    val result = readInput("day13-input.txt")
        .windowed(2, 3)
        .mapIndexed { index, strings ->
            val result = compareList(readList(strings[0]), readList(strings[1]))
            index + 1 to result
        }
        .filter { it.second == 1 }
        .map { it.first }
        .sum()

    println(result)

}

data class Listing(val children: List<Listing>?, val value: Int?)

fun compareList(left: Listing, right: Listing): Int {

    // Both are numbers
    if (left.value != null && right.value != null) {
        if (left.value < right.value)
            return 1
        if (left.value > right.value)
            return -1
        return 0
    }

    // Both are lists
    if (left.children != null && right.children != null) {
        left.children.indices.forEach() { index ->

            // Right run out of elements
            if (index >= right.children.size)
                return -1

            val compare = compareList(left.children[index], right.children[index])
            if (compare != 0)
                return compare

            // Left out of items
            if (index == left.children.size - 1 && right.children.size >= index + 1) {
                return 1
            }
        }

        if (left.children.isEmpty() && !right.children.isEmpty())
            return 1

        return 0
    }

    // And lastly if one is number other list
    if (left.value != null) {
        return compareList(Listing(listOf(Listing(null, left.value)), null), right)
    }

    if (right.value != null) {
        return compareList(left, Listing(listOf(Listing(null, right.value)), null))
    }

    throw Error("should not end here")
}

fun readList(from: String): Listing {
    val trimmed = from.replace("""\s+""".toRegex(), "")

    // Empty list
    if (trimmed == "[]") {
        return Listing(listOf(), null)
    }

    // Simple list remains, this is either [1] or [1,2,...]
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
        if ("""\d+.*""".toRegex().matches(remains)) {
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

        // Just skip the commas
        if (remains.startsWith(',')) {
            remains = remains.drop(1)
        }
    }

    return Listing(parts, null)
}


