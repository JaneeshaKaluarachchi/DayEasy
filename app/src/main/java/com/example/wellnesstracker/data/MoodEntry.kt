package com.example.wellnesstracker.data

/**
 * Data model for mood journal entries
 * Stores user's mood with emoji, note, and timestamp
 */
data class MoodEntry(
    val id: String = System.currentTimeMillis().toString(),
    val emoji: String,
    val moodName: String,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val moodValue: Int // 1-5 scale for charting (1=sad, 5=happy)
)

