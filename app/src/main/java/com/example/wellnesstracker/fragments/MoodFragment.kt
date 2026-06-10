package com.example.wellnesstracker.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wellnesstracker.R
import com.example.wellnesstracker.adapters.MoodAdapter
import com.example.wellnesstracker.data.MoodEntry
import com.example.wellnesstracker.databinding.FragmentMoodBinding
import com.example.wellnesstracker.utils.PreferencesManager
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment for mood journaling with emoji selector
 * Users can log their mood and view mood history
 */
class MoodFragment : Fragment() {

    private var _binding: FragmentMoodBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var moodAdapter: MoodAdapter
    private val moods = mutableListOf<MoodEntry>()

    private var selectedEmoji: String? = null
    private var selectedMoodName: String? = null
    private var selectedMoodValue: Int = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesManager = PreferencesManager(requireContext())
        setupMoodSelector()
        setupRecyclerView()
        loadMoods()
        setupClickListeners()
    }

    private fun setupMoodSelector() {
        // Set click listeners for mood emojis
        binding.moodVeryHappy.setOnClickListener { selectMood("😄", "Very Happy", 5) }
        binding.moodHappy.setOnClickListener { selectMood("😊", "Happy", 4) }
        binding.moodNeutral.setOnClickListener { selectMood("😐", "Neutral", 3) }
        binding.moodSad.setOnClickListener { selectMood("😢", "Sad", 2) }
        binding.moodAngry.setOnClickListener { selectMood("😡", "Angry", 1) }
        binding.moodLove.setOnClickListener { selectMood("😍", "Loving", 5) }
        binding.moodTired.setOnClickListener { selectMood("😴", "Tired", 2) }
        binding.moodExcited.setOnClickListener { selectMood("🤩", "Excited", 5) }
    }

    private fun selectMood(emoji: String, name: String, value: Int) {
        selectedEmoji = emoji
        selectedMoodName = name
        selectedMoodValue = value

        // Visual feedback - reset all
        resetMoodSelection()

        // Highlight selected mood
        when (emoji) {
            "😄" -> binding.moodVeryHappy.alpha = 1.0f
            "😊" -> binding.moodHappy.alpha = 1.0f
            "😐" -> binding.moodNeutral.alpha = 1.0f
            "😢" -> binding.moodSad.alpha = 1.0f
            "😡" -> binding.moodAngry.alpha = 1.0f
            "😍" -> binding.moodLove.alpha = 1.0f
            "😴" -> binding.moodTired.alpha = 1.0f
            "🤩" -> binding.moodExcited.alpha = 1.0f
        }

        binding.selectedMoodText.text = "$emoji $name"
        binding.selectedMoodText.visibility = View.VISIBLE
    }

    private fun resetMoodSelection() {
        binding.moodVeryHappy.alpha = 0.5f
        binding.moodHappy.alpha = 0.5f
        binding.moodNeutral.alpha = 0.5f
        binding.moodSad.alpha = 0.5f
        binding.moodAngry.alpha = 0.5f
        binding.moodLove.alpha = 0.5f
        binding.moodTired.alpha = 0.5f
        binding.moodExcited.alpha = 0.5f
    }

    private fun setupRecyclerView() {
        moodAdapter = MoodAdapter(
            moods = moods,
            onShareClick = { mood ->
                shareMood(mood)
            }
        )

        binding.moodHistoryRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = moodAdapter
        }
    }

    private fun loadMoods() {
        moods.clear()
        moods.addAll(preferencesManager.getMoods().sortedByDescending { it.timestamp })
        moodAdapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun setupClickListeners() {
        binding.btnSaveMood.setOnClickListener {
            saveMood()
        }
    }

    private fun saveMood() {
        if (selectedEmoji == null) {
            Snackbar.make(binding.root, getString(R.string.select_mood), Snackbar.LENGTH_SHORT).show()
            return
        }

        val note = binding.moodNoteInput.text.toString().trim()

        val moodEntry = MoodEntry(
            emoji = selectedEmoji!!,
            moodName = selectedMoodName!!,
            note = note,
            moodValue = selectedMoodValue
        )

        val allMoods = preferencesManager.getMoods()
        allMoods.add(0, moodEntry)
        preferencesManager.saveMoods(allMoods)

        loadMoods()
        clearMoodInput()

        Snackbar.make(binding.root, getString(R.string.mood_saved), Snackbar.LENGTH_SHORT).show()
    }

    private fun clearMoodInput() {
        selectedEmoji = null
        selectedMoodName = null
        selectedMoodValue = 3
        binding.moodNoteInput.text?.clear()
        binding.selectedMoodText.visibility = View.GONE
        resetMoodSelection()
    }

    private fun shareMood(mood: MoodEntry) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
        val dateStr = dateFormat.format(Date(mood.timestamp))

        val shareText = """
            ${mood.emoji} Feeling ${mood.moodName}
            
            ${if (mood.note.isNotEmpty()) "Note: ${mood.note}\n" else ""}
            $dateStr
            
            Tracking my wellness journey with VitalFlow! 🌟
        """.trimIndent()

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
    }

    private fun updateEmptyState() {
        binding.emptyStateText.visibility = if (moods.isEmpty()) View.VISIBLE else View.GONE
        binding.moodHistoryRecycler.visibility = if (moods.isEmpty()) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
