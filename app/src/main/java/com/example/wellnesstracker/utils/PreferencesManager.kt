package com.example.wellnesstracker.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.wellnesstracker.data.Habit
import com.example.wellnesstracker.data.MoodEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val authManager = AuthManager(context)
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "VitalFlowPrefs"
        private const val KEY_HABITS = "_habits"
        private const val KEY_MOODS = "_moods"
        private const val KEY_WATER_COUNT = "_water_count"
        private const val KEY_WATER_GOAL = "_water_goal"
        private const val KEY_REMINDER_ENABLED = "_reminder_enabled"
        private const val KEY_REMINDER_INTERVAL = "_reminder_interval"
        private const val KEY_LAST_RESET_DATE = "_last_reset_date"
        private const val DEFAULT_WATER_GOAL = 8
        private const val DEFAULT_INTERVAL = 60 // 1 hour in minutes
    }


    private fun getUserPrefix(): String {
        val email = authManager.getCurrentUserEmail()
        return if (email.isNotEmpty()) "user_${email}" else "default"
    }

    // Habit Management
    fun saveHabits(habits: List<Habit>) {
        val json = gson.toJson(habits)
        sharedPreferences.edit().putString(getUserPrefix() + KEY_HABITS, json).apply()
    }

    fun getHabits(): MutableList<Habit> {
        val json = sharedPreferences.getString(getUserPrefix() + KEY_HABITS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Habit>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    // Mood Management
    fun saveMoods(moods: List<MoodEntry>) {
        val json = gson.toJson(moods)
        sharedPreferences.edit().putString(getUserPrefix() + KEY_MOODS, json).apply()
    }

    fun getMoods(): MutableList<MoodEntry> {
        val json = sharedPreferences.getString(getUserPrefix() + KEY_MOODS, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<MoodEntry>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    // Hydration Tracking
    fun getWaterCount(): Int {
        checkAndResetDaily()
        return sharedPreferences.getInt(getUserPrefix() + KEY_WATER_COUNT, 0)
    }

    fun setWaterCount(count: Int) {
        sharedPreferences.edit().putInt(getUserPrefix() + KEY_WATER_COUNT, count).apply()
    }

    fun incrementWaterCount() {
        val current = getWaterCount()
        setWaterCount(current + 1)
    }

    fun getWaterGoal(): Int {
        return sharedPreferences.getInt(getUserPrefix() + KEY_WATER_GOAL, DEFAULT_WATER_GOAL)
    }

    fun setWaterGoal(goal: Int) {
        sharedPreferences.edit().putInt(getUserPrefix() + KEY_WATER_GOAL, goal).apply()
    }

    // Reminder Settings
    fun isReminderEnabled(): Boolean {
        return sharedPreferences.getBoolean(getUserPrefix() + KEY_REMINDER_ENABLED, false)
    }

    fun setReminderEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(getUserPrefix() + KEY_REMINDER_ENABLED, enabled).apply()
    }

    fun getReminderInterval(): Int {
        return sharedPreferences.getInt(getUserPrefix() + KEY_REMINDER_INTERVAL, DEFAULT_INTERVAL)
    }

    fun setReminderInterval(intervalMinutes: Int) {
        sharedPreferences.edit().putInt(getUserPrefix() + KEY_REMINDER_INTERVAL, intervalMinutes).apply()
    }

    // Daily Reset Logic
    private fun checkAndResetDaily() {
        val today = getCurrentDateString()
        val lastReset = sharedPreferences.getString(getUserPrefix() + KEY_LAST_RESET_DATE, "")

        if (today != lastReset) {
            resetDailyData()
            sharedPreferences.edit().putString(getUserPrefix() + KEY_LAST_RESET_DATE, today).apply()
        }
    }

    private fun resetDailyData() {
        // Reset water count
        setWaterCount(0)

        // Reset habit completion status
        val habits = getHabits()
        habits.forEach { it.isCompleted = false }
        saveHabits(habits)
    }

    private fun getCurrentDateString(): String {
        val calendar = java.util.Calendar.getInstance()
        return "${calendar.get(java.util.Calendar.YEAR)}-${calendar.get(java.util.Calendar.MONTH)}-${calendar.get(java.util.Calendar.DAY_OF_MONTH)}"
    }

    // Clear only current user's data
    fun clearAllData() {
        val userPrefix = getUserPrefix()
        sharedPreferences.edit().apply {
            remove(userPrefix + KEY_HABITS)
            remove(userPrefix + KEY_MOODS)
            remove(userPrefix + KEY_WATER_COUNT)
            remove(userPrefix + KEY_WATER_GOAL)
            remove(userPrefix + KEY_REMINDER_ENABLED)
            remove(userPrefix + KEY_REMINDER_INTERVAL)
            remove(userPrefix + KEY_LAST_RESET_DATE)
            apply()
        }
    }
}
