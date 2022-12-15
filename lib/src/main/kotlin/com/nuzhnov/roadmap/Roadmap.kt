package com.nuzhnov.roadmap

/**
 * Класс дорожной карты.
 *
 * @property vertices Вершины дорожной карты.
 * @property arcs Дуги дорожной карты.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class Roadmap {

    private val _vertices = mutableListOf<IVertex>()
    private val matrix = mutableListOf(mutableListOf<Arc?>())

    val vertices: Iterable<IVertex> = _vertices
    val arcs: Iterable<Arc> get() = matrix.flatten().filterNotNull()


    /** Построение дорожной карты с вершинами [vertices] без дуг. */
    constructor(vertices: Iterable<IVertex>) {
        addVertices(vertices)
    }

    /** Построение дорожной карты с вершинами [vertex] без дуг. */
    constructor(vararg vertex: IVertex) {
        addVertices(*vertex)
    }

    /** Построение дорожной карты с входными вершинами - [inputVertices],
     * выходными вершинами - [outputVertices] и дугами - [arcs].
     */
    constructor(
        inputVertices: Iterable<IVertex>,
        outputVertices: Iterable<IVertex>,
        arcs: Iterable<Arc>
    ) {
        setArcs(
            outputVertices = outputVertices,
            inputVertices = inputVertices,
            arcs = arcs
        )
    }


    // Auxiliary methods:

    private fun getVertexIndex(vertex: IVertex) = _vertices.indexOf(vertex)

    // Vertex methods:

    /** Содержится ли вершина [vertex] в данной дорожной карте. */
    operator fun contains(vertex: IVertex) = vertex in _vertices

    /** Добавляет вершину [vertex] в дорожную карту. */
    fun addVertex(vertex: IVertex) {
        if (vertex !in _vertices) {
            matrix.add(MutableList(_vertices.size) { null })
            matrix.forEach { row -> row.add(null) }
            _vertices.add(vertex)
        }
    }

    /** Добавляет вершины [vertices] в дорожную карту. */
    fun addVertices(vertices: Iterable<IVertex>) = vertices.forEach { addVertex(it) }
    /** Добавляет вершины [vertex] в дорожную карту. */
    fun addVertices(vararg vertex: IVertex) = vertex.forEach { addVertex(it) }

    /** Удаляет вершину [vertex] из дорожной карты. */
    fun removeVertex(vertex: IVertex) {
        val vertexIndex = getVertexIndex(vertex)

        if (vertexIndex >= 0) {
            matrix.removeAt(vertexIndex)
            matrix.forEach { row -> row.removeAt(vertexIndex) }
            _vertices.removeAt(vertexIndex)
        }
    }

    /** Удаляет вершины [vertices] из дорожной карты. */
    fun removeVertices(vertices: Iterable<IVertex>) = vertices.forEach { removeVertex(it) }
    /** Удаляет вершины [vertex] из дорожной карты. */
    fun removeVertices(vararg vertex: IVertex) = vertex.forEach { removeVertex(it) }

    /** Удаляет все вершины и дуги дорожной карты. */
    fun clear() {
        matrix.clear()
        _vertices.clear()
    }

    // Arc methods:

    /** Получить дугу дорожной карты по её выходной вершине - [outputVertex]
     * и входной вершине - [inputVertex].
     */
    operator fun get(outputVertex: IVertex, inputVertex: IVertex) = matrix
        .getOrNull(getVertexIndex(outputVertex))
        ?.getOrNull(getVertexIndex(inputVertex))

    /**
     * Получить все дуги дорожной карты с выходными вершинами [outputVertices] и
     * соответствующими входными вершинами - [inputVertices].
     */
    fun getArcs(
        outputVertices: Iterable<IVertex>,
        inputVertices: Iterable<IVertex>
    ): Iterable<Arc> = outputVertices
        .zip(inputVertices)
        .mapNotNull { (outputVertex, inputVertex) -> get(
            outputVertex = outputVertex,
            inputVertex = inputVertex
        ) }

    /**
     * Получить все дуги дорожной карты с выходной вершиной [vertex].
     */
    fun getAllOutputArcs(vertex: IVertex): Iterable<Arc>? = matrix
        .getOrNull(getVertexIndex(vertex))
        ?.filterNotNull()

    /**
     * Содержится ли дуга в дорожной карте с выходной и входной вершинами [vertices],
     * где первая вершина - выходная, а вторая - входная.
     */
    operator fun contains(vertices: Pair<IVertex, IVertex>) =
        get(outputVertex = vertices.first, inputVertex = vertices.second) != null

    /**
     * Добавить или удалить дугу из дорожной карты с выходной вершиной [outputVertex],
     * входной вершиной [inputVertex] и объектом дуги [arc]. Поведение данного метода следующее:
     * * Если [arc] != null, то дуга добавится в дорожную карту.
     *   Если при этом какой-либо вершины нет, то эта вершина добавляется в дорожную карту.
     * * Если [arc] == null, то дуга будет удалена.
     *   При этом также, если какой-либо вершины нет, то она также будет добавлена в дорожную карту.
     */
    operator fun set(outputVertex: IVertex, inputVertex: IVertex, arc: Arc?) {
        addVertex(outputVertex)
        addVertex(inputVertex)

        val outputVertexIndex = getVertexIndex(outputVertex)
        val inputVertexIndex = getVertexIndex(inputVertex)

        matrix[outputVertexIndex][inputVertexIndex] = arc
    }

    /**
     * Добавить или удалить дуги из дорожной карты с выходными вершинами [outputVertices],
     * соответствующими входными вершинами [inputVertices] и соответствующими объектами дуг [arcs].
     * Поведение данного метода следующее:
     * * Если элемент [arcs] != null, то дуга добавится в дорожную карту.
     *   Если при этом выходной или входной вершин нет, то эта вершина добавляется в дорожную карту.
     * * Если элемент [arcs] == null, то дуга будет удалена.
     *   При этом также, если выходной или входной вершин нет, то эта вершина также будет добавлена в дорожную карту.
     */
    fun setArcs(
        outputVertices: Iterable<IVertex>,
        inputVertices: Iterable<IVertex>,
        arcs: Iterable<Arc?>
    ) = outputVertices.zip(inputVertices).zip(arcs).forEach { (vertices, arc) ->
        val (outputVertex, inputVertex) = vertices

        set(
            outputVertex = outputVertex,
            inputVertex = inputVertex,
            arc = arc
        )
    }

    /**
     * Удаляет все дуги, связанный с вершиной [vertex],
     * т.н. которые являются выходными или входными по отношению к данной вершине.
     */
    fun clear(vertex: IVertex) {
        val vertexIndex = getVertexIndex(vertex)

        matrix.removeAt(vertexIndex)
        matrix.forEach { row -> row.removeAt(vertexIndex) }
    }
}
