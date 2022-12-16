package day2c

import utils.readInput
import utils.splitOn

fun part1(): Int {
    return 2;
//    return readInput("day2-part1.txt")
//         k   .map { parse(it) }
//            .map { calculateScore(it) }
}

enum class Hand { ROCK, PAPER, SCISSORS }
data class Play(val opponent: Hand, val you: Hand)

fun parse(it: String): Play {
    val parts = it.split(" ")
    return Play(when(parts[0]) {
        "A" -> Hand.ROCK
        "B" -> Hand.PAPER
        "C" -> Hand.SCISSORS
        else -> throw Error("unknown")
    },
            when(parts[1]){
                "X" -> Hand.ROCK
                "Y" -> Hand.PAPER
                else -> throw Error("unknown")
            })

}

fun calculateScore(it: String): Int {
return 1;

}

fun part2(): Int {
    return 2;
}
