@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson5

import lesson5.impl.GraphBuilder
import java.util.*


/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 */
fun Graph.findEulerLoop(): List<Graph.Edge> {
    val result = mutableListOf<Graph.Edge>()
    var n = 0
    for (v in this.vertices) {
        n += this.getNeighbors(v).size % 2
    }
    if (n != 0) return result
    val stack: Stack<Pair<Graph.Vertex, Graph.Edge>> = Stack()
    stack.push(Pair(this.edges.first().begin, this.edges.first()))
    val passed = mutableSetOf(stack.peek().second)
    while (!stack.empty()) {
        var neighbor = 0
        var nextEdge = stack.peek().second
        var nextVertex = stack.peek().first
        when (stack.peek().first) {
            stack.peek().second.begin -> nextVertex = stack.peek().second.end
            stack.peek().second.end -> nextVertex = stack.peek().second.begin
        }
        for (t in getConnections(nextVertex).values) {
            if (!passed.contains(t)) {
                neighbor++
                nextEdge = t
            }
        }
        if (neighbor == 0) {
            result += stack.pop().second
        } else {
            stack.push(Pair(nextVertex, nextEdge))
            passed += nextEdge
        }
    }
    return result

}

/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 */
fun Graph.minimumSpanningTree(): Graph {
    val result: MutableList<Graph.Vertex> = ArrayList()
    val vertices: MutableSet<Graph.Vertex> = this.vertices
    var neighbors: MutableSet<Graph.Vertex> = HashSet()
    val graphR = GraphBuilder()
    var current: Graph.Vertex
    current = vertices.iterator().next()
    result.add(current)
    for (i in 0 until vertices.size - 1) {
        neighbors.addAll(this.getNeighbors(current))
        val iterator: Iterator<Graph.Vertex> = neighbors.iterator()
        while (iterator.hasNext() && result.contains(current)) {
            current = iterator.next()
        }
        if (result.size > 2 && current == result[result.size - 2]) break
        graphR.addVertex(current.toString())
        if (!result.isEmpty()) graphR.addConnection(result[result.size - 1], current, 1)
        result.add(current)
        neighbors.clear()
    }
    neighbors.clear()
    vertices.removeAll(result)
    for (vertex in vertices) {
        neighbors = this.getNeighbors(vertex)
        for (v in neighbors) {

            if (result.contains(v)) {
                result.add(vertex)
                graphR.addVertex(vertex.toString())
                graphR.addConnection(v, vertex, 1)
                break
            }
        }
    }
    return graphR.build()
}
//R = O(n)
//T = O(n)
//где n количество вершин в графе

/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Эта задача может быть зачтена за пятый и шестой урок одновременно
 */
fun Graph.largestIndependentVertexSet(): Set<Graph.Vertex> {
    if (this.vertices.isEmpty()) return emptySet()
    val firstVert = this.vertices.first()
    val storage = mutableMapOf<Graph.Vertex, Set<Graph.Vertex>>()
    return largestIndependentVertexSet(storage, firstVert, null)
}

fun Graph.largestIndependentVertexSet(result: MutableMap<Graph.Vertex, Set<Graph.Vertex>>,
                                      vertex: Graph.Vertex,
                                      curr: Graph.Vertex?): Set<Graph.Vertex> {
    return result.getOrPut(vertex) {
        val neight = this.getNeighbors(vertex).filter { it != curr }
        val neightbors = hashSetOf<Graph.Vertex>()
        val prevPrevSet = hashSetOf<Graph.Vertex>()
        for (prev in neight) {
            neightbors.addAll(this.largestIndependentVertexSet(result, prev, vertex))
            for (prevPrev in this.getNeighbors(prev)) {
                if (prevPrev != vertex) {
                    prevPrevSet.addAll(this.largestIndependentVertexSet(result, prevPrev, prev))
                }
            }
        }
        if (neightbors.size > prevPrevSet.size + 1) neightbors
        else prevPrevSet + vertex
    }
}
//T = O(n + e)
//R = O(n^2)
//где n - количество вершин
// и e - количетво ребер

/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 */
fun Graph.longestSimplePath(): Path {
    TODO()
}