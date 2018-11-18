package lesson3

import java.util.*
import kotlin.NoSuchElementException

// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0
        private set


    private class Node<T>(var value: T) {
        var left: Node<T>? = null

        var right: Node<T>? = null
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
            root?.let { checkInvariant(it) } ?: true

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    private fun delete(node: Node<T>, element: T): Node<T>? {
        val comparison = element.compareTo(node.value)
        if (comparison < 0) node.left = delete(node.left!!, element)
        else if (comparison > 0) node.right = delete(node.right!!, element)
        else {
            if (node.left != null && node.right != null) {
                var clone = node.right
                while (clone?.left != null) {
                    clone = clone.left
                }
                node.value = clone!!.value
                node.right = delete(node.right!!, node.value)
            } else if (node.left != null) return node.left
            else return node.right
        }
        return node
    }

    override fun remove(element: T): Boolean {
        root = delete(root!!, element)
        size--
        return true
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
            root?.let { find(it, value) }

    private fun findParent(element: T): Node<T>? {
        var parent: Node<T>? = null
        var current = root
        while (current != null) {
            val comparison = current.value.compareTo(element)
            if (comparison < 0) {
                parent = current
                current = current.right
            } else if (comparison > 0) {
                parent = current
                current = current.left
            } else break

        }
        return parent
    }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    private fun leftest(element: Node<T>?): Node<T>? {
        var current = element
        while (current?.left != null) {
            current = current.left!!
            println(current.value.toString() + " левее")
        }
        println(current?.value.toString() + " самый левый")
        return current
    }

    inner class BinaryTreeIterator : MutableIterator<T> {

        private var current: Node<T>? = null
        private var stack: Stack<Node<T>>? = null

        init {
            stack = Stack()
            current = root
            stack?.push(root)
            while (current?.left != null) {
                current = current?.left
                stack?.push(current)
            }
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */
        private fun findNext(): Node<T>? {
            if (stack!!.isEmpty()) return null
            current = stack?.pop()
            var fork = current

            if (fork?.right != null) {
                fork = fork.right
                stack!!.push(fork)
                while (fork?.left != null) {
                    fork = fork.left
                    stack!!.push(fork)
                }
            }
            return current
        }

        //T=O(n)
        //R=O(n)
        //где n - высота дерева

        override fun hasNext(): Boolean = findNext() != null

        override fun next(): T {
            findNext()
            return (current ?: throw NoSuchElementException()).value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            TODO()
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}
