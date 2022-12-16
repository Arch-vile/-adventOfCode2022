package utils.graphs

import utils.SortedLookup


// Not immutable by any means
data class Node<T>(val value: T) {

    val edges: MutableSet<Edge<T>> = mutableSetOf()

    fun link(distance: Long, target: Node<T>) {
        edges.add(Edge(distance, target))
    }

    fun link(distance: Long, targets: List<Node<T>>) {
        targets.forEach { link(distance, it) }
    }

    fun biLink(distance: Long, targets: List<Node<T>>) {
        targets.forEach { biLink(distance, it) }
    }

    fun biLink(distance: Long, other: Node<T>) {
        link(distance, other)
        other.link(distance, this)
    }

    override fun toString(): String {
        return "Node(value=$value, edges=$edges)"
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}

data class Edge<T>(var distance: Long, var target: Node<T>) {
    override fun toString(): String {
        return "Edge(distance=$distance, target=Node(${target.value}))"
    }
}

fun <T> shortestPath(start: Node<T>, target: Node<T>): Long? {

    val unvisitedNodes = SortedLookup<Node<T>, Long>()

    allNodes(start)
        .forEach {
            unvisitedNodes.add(it, Long.MAX_VALUE)
        }
    unvisitedNodes.add(start, 0)

    var current = start
    while (true) {
        val edges = current.edges.filter { unvisitedNodes.containsKey(it.target) }
        edges.forEach { edge ->
            val newDistance = unvisitedNodes.get(current)!! + edge.distance
            if (newDistance < unvisitedNodes.get(edge.target)!!)
                unvisitedNodes.add(edge.target, newDistance)
        }
        val removed = unvisitedNodes.drop(current)

        if (removed.first === target) {
            return removed.second
        }
        else {
            current = unvisitedNodes.sortedSequence()
                .filter { unvisitedNodes.containsKey(it.first) }
                .first().first
        }
    }
}

fun <T> route(
    start: Node<T>,
    target: Node<T>,
    tentativeDistances: MutableMap<Node<T>, Long>
): List<Node<T>> {
    return backtrack(target, start, tentativeDistances)
}

fun <T> backtrack(
    current: Node<T>,
    target: Node<T>,
    tentativeDistances: MutableMap<Node<T>, Long>
): List<Node<T>> {
    if (current == target) {
        return listOf()
    }

    val next = current.edges.minByOrNull { tentativeDistances[it.target]!! }!!.target
    return listOf(next).plus(route(next, target, tentativeDistances))
}

fun <T> allNodes(start: Node<T>): Set<Node<T>> {

    val unvisited = mutableSetOf(start)
    val visited = mutableSetOf<Node<T>>()

    while (unvisited.isNotEmpty()) {
        val current = unvisited.first()
        unvisited.remove(current)
        current.edges
            .filter { !visited.contains(it.target) }
            .forEach { unvisited.add(it.target) }
        visited.add(current)
    }

    return visited
}

