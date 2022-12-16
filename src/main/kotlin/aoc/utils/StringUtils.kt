package aoc.utils

fun sort(value: String) = value.toCharArray().sorted().joinToString("")

fun String.toList() = toCharArray().toList()
