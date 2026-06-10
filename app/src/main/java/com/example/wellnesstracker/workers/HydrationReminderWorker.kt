package com.example.wellnesstracker.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.wellnesstracker.utils.NotificationHelper

/**
 * WorkManager worker for periodic hydration reminders
 * Sends notifications at scheduled intervals
 */
class HydrationReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Show hydration reminder notification
        val notificationHelper = NotificationHelper(applicationContext)
        notificationHelper.showHydrationReminder()

        return Result.success()
    }
}

