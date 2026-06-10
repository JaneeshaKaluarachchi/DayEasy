package com.example.wellnesstracker.data

/**
 * Data model for a wellness habit
 * Represents a daily habit that users can track
 */
data class Habit(
    val id: String = System.currentTimeMillis().toString(),
    val name: String,
    var isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

