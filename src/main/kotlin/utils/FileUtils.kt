package utils

import java.io.File

fun read(path: String): List<String> {
   return File(path).readLines()
}

fun readInput(fileName: String) = read("./src/main/resources/${fileName}")
