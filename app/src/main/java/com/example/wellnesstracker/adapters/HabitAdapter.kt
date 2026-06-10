package com.example.wellnesstracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wellnesstracker.data.Habit
import com.example.wellnesstracker.databinding.ItemHabitBinding

/**
 * RecyclerView adapter for displaying and managing habits
 */
class HabitAdapter(
    private val habits: List<Habit>,
    private val onHabitChecked: (Habit, Boolean) -> Unit,
    private val onEditClick: (Habit) -> Unit,
    private val onDeleteClick: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    inner class HabitViewHolder(private val binding: ItemHabitBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(habit: Habit) {
            binding.habitName.text = habit.name
            binding.habitCheckbox.isChecked = habit.isCompleted

            // Set checkbox listener
            binding.habitCheckbox.setOnCheckedChangeListener { _, isChecked ->
                onHabitChecked(habit, isChecked)
            }

            // Set edit button listener
            binding.btnEditHabit.setOnClickListener {
                onEditClick(habit)
            }

            // Set delete button listener
            binding.btnDeleteHabit.setOnClickListener {
                onDeleteClick(habit)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val binding = ItemHabitBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HabitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount() = habits.size
}
