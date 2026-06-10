package com.example.wellnesstracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.work.*
import com.example.wellnesstracker.R
import com.example.wellnesstracker.databinding.FragmentHydrationBinding
import com.example.wellnesstracker.utils.PreferencesManager
import com.example.wellnesstracker.workers.HydrationReminderWorker
import com.google.android.material.snackbar.Snackbar
import java.util.concurrent.TimeUnit

/**
 * Fragment for tracking water intake and managing hydration reminders
 */
class HydrationFragment : Fragment() {

    private var _binding: FragmentHydrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferencesManager: PreferencesManager

    companion object {
        private const val WORK_TAG = "hydration_reminder"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHydrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesManager = PreferencesManager(requireContext())
        setupUI()
        setupClickListeners()
        updateDisplay()
    }

    private fun setupUI() {
        // Set reminder switch state
        binding.switchReminders.isChecked = preferencesManager.isReminderEnabled()
    }

    private fun setupClickListeners() {
        binding.btnAddGlass.setOnClickListener {
            addWaterGlass()
        }

        binding.btnReset.setOnClickListener {
            resetWaterCount()
        }

        binding.switchReminders.setOnCheckedChangeListener { _, isChecked ->
            preferencesManager.setReminderEnabled(isChecked)

            if (isChecked) {
                scheduleHydrationReminder()
                Snackbar.make(binding.root, "Reminders enabled!", Snackbar.LENGTH_SHORT).show()
            } else {
                cancelHydrationReminder()
                Snackbar.make(binding.root, "Reminders disabled", Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnIncreaseGoal.setOnClickListener {
            val currentGoal = preferencesManager.getWaterGoal()
            preferencesManager.setWaterGoal(currentGoal + 1)
            updateDisplay()
        }

        binding.btnDecreaseGoal.setOnClickListener {
            val currentGoal = preferencesManager.getWaterGoal()
            if (currentGoal > 1) {
                preferencesManager.setWaterGoal(currentGoal - 1)
                updateDisplay()
            }
        }
    }

    private fun addWaterGlass() {
        preferencesManager.incrementWaterCount()
        updateDisplay()

        val currentCount = preferencesManager.getWaterCount()
        val goal = preferencesManager.getWaterGoal()

        if (currentCount >= goal) {
            Snackbar.make(binding.root, "🎉 Goal achieved! Great job staying hydrated!", Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(binding.root, "💧 Keep it up!", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun resetWaterCount() {
        preferencesManager.setWaterCount(0)
        updateDisplay()
        Snackbar.make(binding.root, "Water count reset", Snackbar.LENGTH_SHORT).show()
    }

    private fun updateDisplay() {
        val currentCount = preferencesManager.getWaterCount()
        val goal = preferencesManager.getWaterGoal()

        binding.waterCountText.text = currentCount.toString()
        binding.waterGoalText.text = "Goal: $goal glasses"

        // Update progress
        val progress = ((currentCount.toFloat() / goal.toFloat()) * 100).toInt().coerceAtMost(100)
        binding.hydrationProgress.progress = progress
        binding.progressPercentage.text = "$progress%"

        // Visual feedback
        if (currentCount >= goal) {
            binding.waterCountText.setTextColor(resources.getColor(R.color.success, null))
        } else {
            binding.waterCountText.setTextColor(resources.getColor(R.color.primary, null))
        }
    }

    private fun scheduleHydrationReminder() {
        val intervalMinutes = preferencesManager.getReminderInterval().toLong()

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<HydrationReminderWorker>(
            intervalMinutes,
            TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .addTag(WORK_TAG)
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            WORK_TAG,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun cancelHydrationReminder() {
        WorkManager.getInstance(requireContext()).cancelAllWorkByTag(WORK_TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
