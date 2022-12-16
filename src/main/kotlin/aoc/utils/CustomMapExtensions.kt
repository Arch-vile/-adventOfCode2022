package aoc.utils

/**
 * Merges map values (with same key)
 */
fun <K, V> merge(maps: List<Map<K, V>>, merger: (K, V, V) -> V): Map<K, V> {
    return maps.reduce { acc, next -> merge(acc, next, merger) }
}

/**
 * Merges map values (with same key)
 */
fun <K, V> merge(map1: Map<K, V>, map2: Map<K, V>, merger: (K, V, V) -> V): Map<K, V> {
    val mutable = map1.toMutableMap()
    map2.forEach {
        if (map1.containsKey(it.key)) {
            mutable[it.key] = merger(it.key, it.value, map1[it.key]!!)
        }
        else
            mutable[it.key] = it.value
    }
    return mutable.toMap()
}
