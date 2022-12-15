package com.nuzhnov.roadmap

/**
 * Узловая вершина.
 *
 * @property name Наименование вершины.
 * @property description Описание вершины.
 */
@Suppress("unused")
data class NodeVertex(
    val name: String,
    val description: String
) : IVertex
