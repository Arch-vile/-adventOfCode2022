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
    val root = buildFileSystem()
    val sizes = listSizes(root)
    return sizes.filter { it.size <= 100000 }.sumOf { it.size }
}

fun part2(): Int {
    val root = buildFileSystem()
    val sizes = listSizes(root)

    val sizeAvailable = 70000000 - root.size()
    val sizeNeeded = 30000000 - sizeAvailable

    return sizes.filter { it.size >= sizeNeeded }
        .sortedBy { it.size }
        .first().size
}

fun buildFileSystem(): Directory {
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
    return root
}


data class DirSize(val name: String, val size: Int)

fun listSizes(dir: Directory): List<DirSize> {
    val thisSize = DirSize(dir.name, dir.size())
    val others = dir.dirs.map { listSizes(it) }.flatten()
    return others.plus(thisSize)

}

fun secondPart(it: String): String {
    return it.split(" ")[1]
}

fun firstPart(it: String): String {
    return it.split(" ")[0]
}

