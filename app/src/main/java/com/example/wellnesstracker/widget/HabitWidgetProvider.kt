package com.example.wellnesstracker.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.wellnesstracker.MainActivity
import com.example.wellnesstracker.R
import com.example.wellnesstracker.utils.PreferencesManager

/**
 * Home Screen Widget - Displays today's habit completion percentage
 * Shows motivational message and progress at a glance
 */
class HabitWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // Update all widget instances
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Widget is added for the first time
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        // Last widget instance removed
        super.onDisabled(context)
    }

    companion object {
        /**
         * Update a single widget instance
         */
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // Get habit data
            val preferencesManager = PreferencesManager(context)
            val habits = preferencesManager.getHabits()

            // Calculate progress
            val totalHabits = habits.size
            val completedHabits = habits.count { it.isCompleted }
            val percentage = if (totalHabits > 0) {
                (completedHabits * 100) / totalHabits
            } else {
                0
            }

            // Get motivational message based on progress
            val motivationalMessage = when {
                percentage == 100 -> "🎉 Perfect Day!"
                percentage >= 75 -> "💪 Almost There!"
                percentage >= 50 -> "✨ Keep Going!"
                percentage >= 25 -> "🌟 Good Start!"
                totalHabits == 0 -> "➕ Add Habits"
                else -> "🎯 Let's Begin!"
            }

            // Create intent to launch app when widget is clicked
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Build the widget layout
            val views = RemoteViews(context.packageName, R.layout.widget_habit_tracker)

            // Update widget content
            views.setTextViewText(R.id.widget_title, "DayEasy")
            views.setTextViewText(R.id.widget_percentage, "$percentage%")
            views.setTextViewText(R.id.widget_message, motivationalMessage)
            views.setTextViewText(
                R.id.widget_stats,
                "$completedHabits / $totalHabits habits completed"
            )
            views.setProgressBar(R.id.widget_progress, 100, percentage, false)

            // Set click listener
            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent)

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        /**
         * Refresh all widget instances
         */
        fun refreshAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(context, HabitWidgetProvider::class.java)
            )

            for (appWidgetId in appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId)
            }
        }
    }
}
