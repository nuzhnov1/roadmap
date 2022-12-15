package com.nuzhnov.roadmap

/**
 * Вершина знания.
 *
 * @property technologies Список технологий для данной вершины.
 */
@Suppress("unused")
data class KnowledgeVertex(
    val technologies: List<Technology>
) : IVertex
