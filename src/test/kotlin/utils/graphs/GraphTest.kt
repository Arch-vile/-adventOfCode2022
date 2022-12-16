package utils.graphs

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
        var first = Node(1)
        var second =  Node(2)
        var third = Node(3)
        var fourth = Node(4)

        first.link(2,second)
        first.link(3, third)
        third.link(5,second)
        third.link(6,fourth)

       assertEquals(9, shortestPath(first, fourth))
    }


    @Test
    fun shortestPath_complex() {
        var first = Node(1)
        var second =  Node(2)
        var third = Node(3)
        var fourth = Node(4)
        var fifth = Node(5)
        var sixth = Node(6)
        var seventh = Node(7)
        var eight = Node(8)
        var ninth = Node(9)

        // Keep the leaf as the first one and with shortest distance
        fifth.link(1,ninth)
        first.link(2,second)
        first.link(5, third)
        second.link(3, fourth)
        second.link(4, fifth)
        second.link(2, sixth)
        third.link(2, eight)
        fourth.link(5,eight)
        fifth.link(8,eight)
        sixth.link(3,seventh)
        seventh.link(4,eight)

        assertEquals(7, shortestPath(first, eight))
    }
}
