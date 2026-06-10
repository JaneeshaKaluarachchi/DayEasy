package com.example.wellnesstracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wellnesstracker.data.MoodEntry
import com.example.wellnesstracker.databinding.ItemMoodBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * RecyclerView adapter for displaying mood history
 */
class MoodAdapter(
    private val moods: List<MoodEntry>,
    private val onShareClick: (MoodEntry) -> Unit
) : RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    private val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())

    inner class MoodViewHolder(private val binding: ItemMoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mood: MoodEntry) {
            binding.moodEmoji.text = mood.emoji
            binding.moodName.text = mood.moodName
            binding.moodTimestamp.text = dateFormat.format(Date(mood.timestamp))

            if (mood.note.isNotEmpty()) {
                binding.moodNote.text = mood.note
                binding.moodNote.visibility = android.view.View.VISIBLE
            } else {
                binding.moodNote.visibility = android.view.View.GONE
            }

            binding.btnShareMood.setOnClickListener {
                onShareClick(mood)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val binding = ItemMoodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        holder.bind(moods[position])
    }

    override fun getItemCount() = moods.size
}

