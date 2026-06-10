package com.example.wellnesstracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.wellnesstracker.R
import com.example.wellnesstracker.databinding.FragmentAnalyticsBinding
import com.example.wellnesstracker.utils.PreferencesManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment for displaying analytics and mood trends
 * Uses MPAndroidChart to visualize mood data over the past 7 days
 */
class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            preferencesManager = PreferencesManager(requireContext())
            setupChart()
            loadAnalytics()
        } catch (e: Exception) {
            e.printStackTrace()
            binding.noDataText.visibility = View.VISIBLE
            binding.moodChart.visibility = View.GONE
        }
    }

    private fun setupChart() {
        try {
            binding.moodChart.apply {
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
                setDrawGridBackground(false)

                // X-axis setup
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawGridLines(false)
                    granularity = 1f
                    textColor = ContextCompat.getColor(requireContext(), R.color.text_primary)
                }

                // Y-axis setup
                axisLeft.apply {
                    setDrawGridLines(true)
                    axisMinimum = 0f
                    axisMaximum = 5f
                    granularity = 1f
                    textColor = ContextCompat.getColor(requireContext(), R.color.text_primary)
                }

                axisRight.isEnabled = false
                legend.textColor = ContextCompat.getColor(requireContext(), R.color.text_primary)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadAnalytics() {
        try {
            val moods = preferencesManager.getMoods()
            val habits = preferencesManager.getHabits()

            if (moods.isEmpty()) {
                binding.noDataText.visibility = View.VISIBLE
                binding.moodChart.visibility = View.GONE
            } else {
                binding.noDataText.visibility = View.GONE
                binding.moodChart.visibility = View.VISIBLE
                displayMoodTrends(moods)
            }

            displayHabitStats(habits)
            displayWeeklySummary(moods, habits)
        } catch (e: Exception) {
            e.printStackTrace()
            binding.noDataText.visibility = View.VISIBLE
            binding.moodChart.visibility = View.GONE
        }
    }

    private fun displayMoodTrends(moods: List<com.example.wellnesstracker.data.MoodEntry>) {
        try {
            // Get last 7 days of mood data
            val calendar = Calendar.getInstance()
            val today = calendar.timeInMillis
            val sevenDaysAgo = today - (7 * 24 * 60 * 60 * 1000)

            val recentMoods = moods.filter { it.timestamp >= sevenDaysAgo }

            if (recentMoods.isEmpty()) {
                binding.noDataText.visibility = View.VISIBLE
                binding.moodChart.visibility = View.GONE
                return
            }

            // Group moods by day and calculate average
            val moodsByDay = mutableMapOf<String, MutableList<Int>>()
            val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())

            recentMoods.forEach { mood ->
                val day = dateFormat.format(Date(mood.timestamp))
                if (!moodsByDay.containsKey(day)) {
                    moodsByDay[day] = mutableListOf()
                }
                moodsByDay[day]?.add(mood.moodValue)
            }

            // Create chart entries
            val entries = mutableListOf<Entry>()
            val labels = mutableListOf<String>()

            moodsByDay.entries.sortedBy { it.key }.forEachIndexed { index, entry ->
                val avgMood = entry.value.average().toFloat()
                entries.add(Entry(index.toFloat(), avgMood))
                labels.add(entry.key)
            }

            if (entries.isEmpty()) {
                binding.noDataText.visibility = View.VISIBLE
                binding.moodChart.visibility = View.GONE
                return
            }

            // Create dataset
            val dataSet = LineDataSet(entries, "Mood Level (1-5)").apply {
                color = ContextCompat.getColor(requireContext(), R.color.accent_10)
                setCircleColor(ContextCompat.getColor(requireContext(), R.color.accent_10))
                lineWidth = 3f
                circleRadius = 5f
                setDrawCircleHole(false)
                valueTextSize = 12f
                valueTextColor = ContextCompat.getColor(requireContext(), R.color.text_primary)
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawFilled(true)
                fillColor = ContextCompat.getColor(requireContext(), R.color.accent_light)
                fillAlpha = 100
            }

            // Set data
            val lineData = LineData(dataSet)
            binding.moodChart.data = lineData

            // Set custom X-axis labels
            binding.moodChart.xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return if (value.toInt() >= 0 && value.toInt() < labels.size) {
                        labels[value.toInt()]
                    } else {
                        ""
                    }
                }
            }

            binding.moodChart.invalidate()
        } catch (e: Exception) {
            e.printStackTrace()
            binding.noDataText.visibility = View.VISIBLE
            binding.moodChart.visibility = View.GONE
        }
    }

    private fun displayHabitStats(habits: List<com.example.wellnesstracker.data.Habit>) {
        try {
            if (habits.isEmpty()) {
                binding.habitCompletionText.text = "No habits tracked yet"
                return
            }

            val completed = habits.count { it.isCompleted }
            val total = habits.size
            val percentage = if (total > 0) (completed * 100) / total else 0

            binding.habitCompletionText.text = """
            Today's Habits: $completed / $total completed
            Completion Rate: $percentage%
        """.trimIndent()
        } catch (e: Exception) {
            e.printStackTrace()
            binding.habitCompletionText.text = "Unable to load habit stats"
        }
    }

    private fun displayWeeklySummary(
        moods: List<com.example.wellnesstracker.data.MoodEntry>,
        habits: List<com.example.wellnesstracker.data.Habit>
    ) {
        try {
            val calendar = Calendar.getInstance()
            val today = calendar.timeInMillis
            val sevenDaysAgo = today - (7 * 24 * 60 * 60 * 1000)

            val recentMoods = moods.filter { it.timestamp >= sevenDaysAgo }
            val totalHabits = habits.size

            val summary = """
            📊 Weekly Summary
            
            🌟 Mood Entries: ${recentMoods.size}
            ✅ Active Habits: $totalHabits
            💧 Hydration Goal: ${preferencesManager.getWaterGoal()} glasses/day
            
            Keep up the great work! 🚀
        """.trimIndent()

            binding.weeklySummaryText.text = summary
        } catch (e: Exception) {
            e.printStackTrace()
            binding.weeklySummaryText.text = "Unable to load weekly summary"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
