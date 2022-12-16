package aoc.utils

data class MutableInt(var value: Int) {

    fun plus(add: Int): Int {
        value+=add
        return value
    }
}
