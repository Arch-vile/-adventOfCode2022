package utils

import java.io.File

fun read(path: String): List<String> {
   return File(path).readLines()
}
