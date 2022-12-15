package com.nuzhnov.roadmap

/**
 * Класс технологии.
 *
 * @property name Наименование технологии.
 * @property description Описание технологии.
 * @property requirementLevel Требуемый уровень знания технологии.
 * @property application Список применений технологии.
 * @property knowledgeSources Список источников изучения технологии.
 */
data class Technology(
    val name: String,
    val description: String,
    val requirementLevel: RequirementLevel,
    val application: List<String>,
    val knowledgeSources: List<String>
)
