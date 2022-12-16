package day7

import aoc.utils.readInput

data class File(val name: String, val size: Int)
data class Directory(
    val name: String,
    val parent: Directory?,
    val dirs: MutableList<Directory> = mutableListOf(),
    val files: MutableList<File> = mutableListOf()
) {
    fun cd(dirName: String): Directory {
        return this.dirs.first { it.name == dirName }
    }

    fun size(): Int {
        val fileSizes = files.sumOf { it.size }
        val dirSizes = dirs.sumOf { it.size() }
        return fileSizes + dirSizes
    }
}


fun part1(): Int {
    val input = readInput("day7-input.txt").drop(1)

    var currentDir = Directory("/", null, mutableListOf())
    val root = currentDir

    input.forEach {

        if (firstPart(it) == "dir") {
            currentDir.dirs.add(Directory(secondPart(it), currentDir))
        }

        if (firstPart(it).matches("\\d*".toRegex())) {
            currentDir.files.add(File(secondPart(it), firstPart(it).toInt()))
        }

        if (it.startsWith("$ cd")) {
            val dirName = it.split(" ")[2]
            if (dirName == "..")
                currentDir = currentDir.parent!!
            else
                currentDir = currentDir.cd(dirName)
        }
    }

    return listSizes(root)
        .filter { secondPart(it).toInt() < 100000 }
        .map { secondPart(it) }
        .sumOf { it.toInt() }


}

fun part2(): Int {



    return 1;
}

fun listSizes(dir: Directory): List<String>  {

    val thisSize = "${dir.name} ${dir.size()}"

    val others = dir.dirs.map { listSizes(it) }.flatten()

    return others.plus(thisSize)

}

fun secondPart(it: String): String {
    return it.split(" ")[1]
}

fun firstPart(it: String): String {
    return it.split(" ")[0]
}


fun command(it: String): String {
    return it.split(" ")[0]
}

