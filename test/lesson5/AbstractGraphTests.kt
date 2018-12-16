package lesson5

import lesson5.Graph.Edge
import lesson5.impl.GraphBuilder
import kotlin.test.assertEquals
import kotlin.test.assertTrue

abstract class AbstractGraphTests {

    private fun Edge.isNeighbour(other: Edge): Boolean {
        return begin == other.begin || end == other.end || begin == other.end || end == other.begin
    }

    private fun List<Edge>.assert(shouldExist: Boolean, graph: Graph) {
        val edges = graph.edges
        if (shouldExist) {
            assertEquals(edges.size, size, "Euler loop should traverse all edges")
        } else {
            assertTrue(isEmpty(), "Euler loop should not exist")
        }
        for (edge in this) {
            assertTrue(edge in edges, "Edge $edge is not inside graph")
        }
        for (i in 0 until size - 1) {
            assertTrue(this[i].isNeighbour(this[i + 1]), "Edges ${this[i]} & ${this[i + 1]} are not incident")
        }
        if (!this.isEmpty()) assertTrue(this[0].isNeighbour(this[size - 1]), "Edges ${this[0]} & ${this[size - 1]} are not incident")
    }

    fun findEulerLoop(findEulerLoop: Graph.() -> List<Edge>) {
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(a, c)
        }.build()
        val loop = graph.findEulerLoop()
        loop.assert(true, graph)
        val graph2 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(a, e)
            addConnection(d, k)
            addConnection(e, j)
            addConnection(j, k)
            addConnection(b, f)
            addConnection(c, i)
            addConnection(f, i)
            addConnection(b, g)
            addConnection(g, h)
            addConnection(h, c)
        }.build()
        val loop2 = graph2.findEulerLoop()
        loop2.assert(true, graph2)
        /* graph3

             A---B
             |   |
             C---D---E
        */
        val graph3 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")

            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(a, d)
            addConnection(e, d)

        }.build()
        val loop3 = graph3.findEulerLoop()
        loop3.assert(false, graph3)

        /* graph4

                A---B
                |   |
            C---D---E---F
            |   |   |   |
        G---H---I---J---K---L
        |   |   |   |   |   |
        M---N---O---P---Q---R
            |   |   |   |
            S---T---U---V
                |   |
                W---X
         */
        val graph4 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            val l = addVertex("L")
            val m = addVertex("M")
            val n = addVertex("N")
            val o = addVertex("O")
            val p = addVertex("P")
            val q = addVertex("Q")
            val r = addVertex("R")
            val s = addVertex("S")
            val t = addVertex("T")
            val u = addVertex("U")
            val v = addVertex("V")
            val w = addVertex("W")
            val x = addVertex("X")
            addConnection(a, b)
            addConnection(a, d)
            addConnection(b, e)
            addConnection(c, d)
            addConnection(c, h)
            addConnection(d, e)
            addConnection(d, i)
            addConnection(e, f)
            addConnection(e, j)
            addConnection(f, k)
            addConnection(g, h)
            addConnection(g, m)
            addConnection(h, i)
            addConnection(h, n)
            addConnection(i, j)
            addConnection(i, o)
            addConnection(j, k)
            addConnection(j, p)
            addConnection(k, l)
            addConnection(k, q)
            addConnection(l, r)
            addConnection(m, n)
            addConnection(n, o)
            addConnection(n, s)
            addConnection(o, p)
            addConnection(o, t)
            addConnection(p, q)
            addConnection(p, u)
            addConnection(q, r)
            addConnection(q, v)
            addConnection(s, t)
            addConnection(t, u)
            addConnection(t, w)
            addConnection(u, v)
            addConnection(u, x)
            addConnection(w, x)
        }.build()
        val loop4 = graph4.findEulerLoop()
        loop4.assert(true, graph4)
    }

    fun minimumSpanningTree(minimumSpanningTree: Graph.() -> Graph) {
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(a, c)
        }.build()
        val tree = graph.minimumSpanningTree()
        assertEquals(2, tree.edges.size)
        assertEquals(2, tree.findBridges().size)
        val graph2 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(a, e)
            addConnection(d, k)
            addConnection(e, j)
            addConnection(j, k)
            addConnection(b, f)
            addConnection(c, i)
            addConnection(f, i)
            addConnection(b, g)
            addConnection(g, h)
            addConnection(h, c)
        }.build()
        val tree2 = graph2.minimumSpanningTree()
        assertEquals(10, tree2.edges.size)
        assertEquals(10, tree2.findBridges().size)
        val graph3 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            addConnection(a, b)
            addConnection(a, c)
            addConnection(b, d)
            addConnection(c, d)
        }.build()
        val tree3 = graph3.minimumSpanningTree()
        assertEquals(3, tree3.edges.size)
        assertEquals(3, tree3.findBridges().size)
        val graph4 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            addConnection(a, b)
            addConnection(a, c)
            addConnection(b, d)
            addConnection(c, e)
            addConnection(c, f)
            addConnection(b, g)
            addConnection(d, i)
            addConnection(g, h)
            addConnection(h, j)
            addConnection(f, i)
        }.build()
        val tree4 = graph4.minimumSpanningTree()
        assertEquals(9, tree4.edges.size)
        assertEquals(9, tree4.findBridges().size)
    }

    fun largestIndependentVertexSet(largestIndependentVertexSet: Graph.() -> Set<Graph.Vertex>) {
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            addConnection(a, b)
            addConnection(a, c)
            addConnection(b, d)
            addConnection(c, e)
            addConnection(c, f)
            addConnection(b, g)
            addConnection(d, i)
            addConnection(g, h)
            addConnection(h, j)
        }.build()
        val independent = graph.largestIndependentVertexSet()
        assertEquals(setOf(graph["A"], graph["D"], graph["E"], graph["F"], graph["G"], graph["J"]),
                independent)

        val graph1 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b)
            addConnection(b, c)
        }.build()
        val independent1 = graph1.largestIndependentVertexSet()
        assertEquals(setOf(graph1["A"], graph1["C"]), independent1)

        val graph2 = GraphBuilder().apply {
            addVertex("A")
        }.build()
        val independent2 = graph2.largestIndependentVertexSet()
        assertEquals(setOf(graph2["A"]), independent2)

        val graph3 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(b, d)
            addConnection(d, e)
            addConnection(g, f)
            addConnection(f, e)
            addConnection(g, h)
        }.build()
        val independent3 = graph3.largestIndependentVertexSet()
        assertEquals(setOf(graph3["A"], graph3["C"], graph3["F"], graph3["H"], graph3["D"]), independent3)
    }

    fun longestSimplePath(longestSimplePath: Graph.() -> Path) {
        val graph = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(a, c)
        }.build()
        val longestPath = graph.longestSimplePath()
        assertEquals(2, longestPath.length)

        val graph2 = GraphBuilder().apply {
            val a = addVertex("A")
            val b = addVertex("B")
            val c = addVertex("C")
            val d = addVertex("D")
            val e = addVertex("E")
            val f = addVertex("F")
            val g = addVertex("G")
            val h = addVertex("H")
            val i = addVertex("I")
            val j = addVertex("J")
            val k = addVertex("K")
            addConnection(a, b)
            addConnection(b, c)
            addConnection(c, d)
            addConnection(a, e)
            addConnection(d, k)
            addConnection(e, j)
            addConnection(j, k)
            addConnection(b, f)
            addConnection(c, i)
            addConnection(f, i)
            addConnection(b, g)
            addConnection(g, h)
            addConnection(h, c)
        }.build()
        val longestPath2 = graph2.longestSimplePath()
        assertEquals(10, longestPath2.length)
    }

}