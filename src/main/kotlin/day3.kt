package day3

import utils.readInput
import utils.splitMiddle
import utils.splitOn

fun part1(): Int {
    return readInput("day3-input.txt")
        .map { it.toCharArray().toList() }
        .map { it.splitMiddle()}
        .map { it.first.intersect(it.second)  }
        .map { it.first() }
        .sumOf { priority(it) }
}

fun part2(): Int {
    return 2;
}

fun priority(it: Char): Int {
    val v = it.toInt()
    if(v < 97) {
        return v - 38
    } else {
        return v - 96
    }
}

