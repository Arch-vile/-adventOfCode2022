package aoc.utils.graphs

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GraphTest {

    @Test
    fun all_nodes_for_single_node() {
        val start = Node(1);
        assertEquals(setOf(start), allNodes(start))
    }

    @Test
    fun all_nodes_for_unidirectional_pair() {
        val start = Node(1)
        val second = Node(2)
        start.link(1, second)

        assertEquals(setOf(start, second), allNodes(start))
    }

    @Test
    fun all_nodes_for_bidirectional_pair() {
        val start = Node(1)
        val second = Node(2)
        start.biLink(1, second)
        assertEquals(setOf(start, second), allNodes(start))
    }

    @Test
    fun all_nodes_for_unidirectional_triplet_graph() {
        val third = Node(3)
        val second = Node(2)
        val start = Node(1)
        start.link(1, third)
        start.link(1, second)
        assertEquals(setOf(start, second, third), allNodes(start))
    }

    @Test
    fun all_nodes_for_bidirectional_triplet_graph() {
        val third = Node(3)
        val second = Node(2)
        val start = Node(1)
        start.biLink(1, third)
        start.biLink(1, second)
        assertEquals(setOf(start, second, third), allNodes(start))
    }

    @Test
    fun all_nodes_for_unidirectional_circular_graph() {
        val second = Node(2)
        val third = Node(3)
        val start = Node(1)
        start.link(1, second)
        start.link(1, third)
        third.link(1, second)
        assertEquals(setOf(start, second, third), allNodes(start))
    }

    @Test
    fun all_nodes_for_bidirectional_circular_graph() {
        val second = Node(2)
        val third = Node(3)
        val start = Node(1)
        start.biLink(1, second)
        start.biLink(1, third)
        second.biLink(1, second)
        assertEquals(setOf(start, second, third), allNodes(start))
    }

    @Test
    fun all_nodes_for_unidirectional_circular_graph_with_leaf() {
        val fourth = Node(4)
        val second = Node(2)
        val third = Node(3)
        third.link(1, second)
        third.link(1, fourth)
        val start = Node(1)
        start.link(1, third)
        start.link(1, second)
        assertEquals(setOf(start, second, third, fourth), allNodes(start))
    }


    @Test
    fun all_nodes_for_bidirectional_circular_graph_with_leaf() {
        val second = Node(2)
        val third = Node(3)
        val start = Node(1)
        val fourth = Node(4)
        start.biLink(1, second)
        start.biLink(1, third)
        second.biLink(1, second)
        fourth.biLink(1,third)
        assertEquals(setOf(start, second, third, fourth), allNodes(start))
    }

    @Test
    fun allNodes_for_bidirectional_matrix() {
       var first = Node(1)
        val second = Node(2)
        val third = Node(3)
        val fourth = Node(4)
        first.biLink(1, listOf(second,third))
        fourth.biLink(1, listOf(second,third))

        assertEquals(setOf(first,second,third,fourth), allNodes(first))
    }

    @Test
    fun shortestPath() {
        /**
         *   A - - 2 - - B
         *   |         /
         *   |      5
         *   3    /
         *   | /
         *   C --6-- D
         */

        var a = Node('a')
        var b =  Node('b')
        var c = Node('c')
        var d = Node('d')

        a.link(2,b)
        a.link(3, c)
        c.link(5,b)
        c.link(6,d)

       assertEquals(9, shortestDistance(a, d))
        assertEquals(listOf(a,c,d), aoc.utils.graphs.shortestPath(a,d))
    }


    @Test
    fun shortestPath_complex() {
        var a = Node('a')
        var b =  Node('b')
        var c = Node('c')
        var d = Node('d')
        var e = Node('e')
        var f = Node('f')
        var g = Node('g')
        var h = Node('h')
        var i = Node('i')

        // Keep the leaf as the first one and with shortest distance
        e.link(1,i)
        a.link(2,b)
        a.link(5, c)
        b.link(3, d)
        b.link(4, e)
        b.link(2, f)
        c.link(2, h)
        d.link(5,h)
        e.link(8,h)
        f.link(3,g)
        g.link(4,h)

        assertEquals(7, shortestDistance(a, h))
        assertEquals(listOf(a,c,h), shortestPath(a,h))

        assertEquals(7, shortestDistance(a, i))
        assertEquals(listOf(a,b,e,i), shortestPath(a,i))

        assertEquals(8, shortestDistance(b, h))
        assertEquals(listOf(b,d,h), shortestPath(b,h))
    }
}
