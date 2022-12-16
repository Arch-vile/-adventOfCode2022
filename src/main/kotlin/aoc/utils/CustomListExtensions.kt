package aoc.utils

/**
 * Identical to takeWhile but also includes the first element that did not match.
 * listOf(1,2,3,4).takeUntil { it < 2 } --> [1,2,3]
 */
fun <T> List<T>.takeUntil(predicate: (T) -> Boolean ): List<T> {
   TODO()
}

/**
 * Split just before the first element matching the predicate. The matching element is included
 * in the latter Pair element
 */
fun <T> List<T>.splitBeforeFirst(predicate: (T) -> Boolean ): Pair<List<T>,List<T>> {
   TODO()
}

fun <T> intersect(data: Collection<Collection<T>>): Collection<T> {
    return data.reduce { acc, list -> acc.intersect(list).toList() }
}

/**
 * Returns all the possible combinations (order does not matter) formed from given values.
 * Each combination will have at least one element.
 *
 * combinations([1,2,3]) =>  [[1], [2], [1, 2], [3], [1, 3], [2, 3], [1, 2, 3]]
 *
 * Duplicates are allowed if you feed in duplicates:
 * combinations([1,2,2]) => [[1], [2], [1, 2], [2, 2], [1, 2, 2]]
 */
fun <T> List<T>.combinations(): Set<List<T>> {
    val subsets = mutableSetOf<List<T>>()
    val n: Int = size
    for (i in 0 until (1 shl n)) {
        val subset = mutableListOf<T>()
        for (j in 0 until n) {
            if (i and (1 shl j) > 0) {
                subset.add(this[j])
            }
        }
        subsets.add(subset)
    }
    return subsets.filter { it.isNotEmpty() }.toSet()
}

/**
 * All order permutations of given values. Contains all given values
 * e.g. 1,2,3 -> 1,2,3 2,3,1 3,1,2 ...
 */
fun <T> permutations(set: Set<T>): Set<List<T>> {
    if (set.isEmpty()) return emptySet()

    fun <T> allPermutations(list: List<T>): Set<List<T>> {
        if (list.isEmpty()) return setOf(emptyList())

        val result: MutableSet<List<T>> = mutableSetOf()
        for (i in list.indices) {
            allPermutations(list - list[i]).forEach { item ->
                result.add(item + list[i])
            }
        }
        return result
    }

    return allPermutations(set.toList())
}

data class SectionCandidate<T>(val section: List<T>, val before: List<T>, val after: List<T> )

/**
 * Find sections within given size limits that satisfy the condition matcher
 */
fun <T> List<T>.findSections(sectionSize: ClosedRange<Int>, matcher: (SectionCandidate<T>) -> Boolean): List<List<T>> {

    val sections = mutableListOf<List<T>>()

    for (i in indices) {
        var sliceSize = sectionSize.start
        while (sectionSize.contains(sliceSize) && i+sliceSize<size) {
            val section = subList(i, i + sliceSize)
            val before = subList(0,i)
            val after = subList(i+sliceSize,size)
            if(matcher(SectionCandidate(section,before,after)))
                sections.add(section)
            sliceSize++
        }
    }


    return sections
}

