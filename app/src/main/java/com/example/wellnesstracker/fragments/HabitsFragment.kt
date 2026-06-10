package com.example.wellnesstracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wellnesstracker.R
import com.example.wellnesstracker.adapters.HabitAdapter
import com.example.wellnesstracker.data.Habit
import com.example.wellnesstracker.databinding.FragmentHabitsBinding
import com.example.wellnesstracker.utils.PreferencesManager
import com.example.wellnesstracker.widget.HabitWidgetProvider
import com.google.android.material.snackbar.Snackbar

/**
 * Fragment for managing daily wellness habits
 * Users can add, edit, delete, and track completion of habits
 */
class HabitsFragment : Fragment() {

    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var habitAdapter: HabitAdapter
    private val habits = mutableListOf<Habit>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesManager = PreferencesManager(requireContext())
        setupRecyclerView()
        loadHabits()
        setupClickListeners()
        updateProgress()
    }

    private fun setupRecyclerView() {
        habitAdapter = HabitAdapter(
            habits = habits,
            onHabitChecked = { habit, isChecked ->
                habit.isCompleted = isChecked
                saveHabits()
                updateProgress()

                if (isChecked) {
                    showMotivationalMessage()
                }
            },
            onEditClick = { habit ->
                showEditHabitDialog(habit)
            },
            onDeleteClick = { habit ->
                showDeleteConfirmation(habit)
            }
        )

        binding.habitsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = habitAdapter
        }
    }

    private fun loadHabits() {
        habits.clear()
        habits.addAll(preferencesManager.getHabits())
        habitAdapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun saveHabits() {
        preferencesManager.saveHabits(habits)
        // Refresh widget to show updated progress
        HabitWidgetProvider.refreshAllWidgets(requireContext())
    }

    private fun setupClickListeners() {
        binding.fabAddHabit.setOnClickListener {
            showAddHabitDialog()
        }
    }

    private fun showAddHabitDialog() {
        val input = EditText(requireContext()).apply {
            hint = getString(R.string.habit_name_hint)
            setPadding(50, 30, 50, 30)
        }

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_add_habit))
            .setView(input)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val habitName = input.text.toString().trim()
                if (habitName.isNotEmpty()) {
                    addHabit(habitName)
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun addHabit(name: String) {
        val newHabit = Habit(name = name)
        habits.add(newHabit)
        saveHabits()
        habitAdapter.notifyItemInserted(habits.size - 1)
        updateProgress()
        updateEmptyState()

        Snackbar.make(binding.root, getString(R.string.motivation_3), Snackbar.LENGTH_SHORT).show()
    }

    private fun showEditDeleteDialog(habit: Habit) {
        val options = arrayOf(getString(R.string.edit_habit), getString(R.string.delete_habit))

        AlertDialog.Builder(requireContext())
            .setTitle(habit.name)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditHabitDialog(habit)
                    1 -> showDeleteConfirmation(habit)
                }
            }
            .show()
    }

    private fun showEditHabitDialog(habit: Habit) {
        val input = EditText(requireContext()).apply {
            setText(habit.name)
            setPadding(50, 30, 50, 30)
        }

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_edit_habit))
            .setView(input)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val index = habits.indexOf(habit)
                    habits[index] = habit.copy(name = newName)
                    saveHabits()
                    habitAdapter.notifyItemChanged(index)
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun showDeleteConfirmation(habit: Habit) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_habit))
            .setMessage(getString(R.string.confirm_delete))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteHabit(habit)
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun deleteHabit(habit: Habit) {
        val index = habits.indexOf(habit)
        habits.removeAt(index)
        saveHabits()
        habitAdapter.notifyItemRemoved(index)
        updateProgress()
        updateEmptyState()
    }

    private fun updateProgress() {
        if (habits.isEmpty()) {
            binding.progressBar.progress = 0
            binding.progressText.text = "0%"
            return
        }

        val completed = habits.count { it.isCompleted }
        val total = habits.size
        val percentage = (completed * 100) / total

        binding.progressBar.progress = percentage
        binding.progressText.text = "$percentage%"
        binding.completionStats.text = "$completed / $total habits completed"

        // Refresh widget to show updated progress
        HabitWidgetProvider.refreshAllWidgets(requireContext())
    }

    private fun updateEmptyState() {
        binding.emptyStateText.visibility = if (habits.isEmpty()) View.VISIBLE else View.GONE
        binding.habitsRecyclerView.visibility = if (habits.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun showMotivationalMessage() {
        val messages = listOf(
            getString(R.string.motivation_1),
            getString(R.string.motivation_2),
            getString(R.string.motivation_3),
            getString(R.string.motivation_4)
        )
        Snackbar.make(binding.root, messages.random(), Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
