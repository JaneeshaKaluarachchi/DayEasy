package com.example.wellnesstracker.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.wellnesstracker.AuthActivity
import com.example.wellnesstracker.R
import com.example.wellnesstracker.databinding.FragmentSettingsBinding
import com.example.wellnesstracker.utils.AuthManager
import com.example.wellnesstracker.utils.PreferencesManager
import com.google.android.material.snackbar.Snackbar

/**
 * Fragment for app settings and preferences
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferencesManager = PreferencesManager(requireContext())
        authManager = AuthManager(requireContext())
        setupClickListeners()
        displayCurrentSettings()
    }

    private fun setupClickListeners() {
        binding.btnResetData.setOnClickListener {
            showResetConfirmation()
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun displayCurrentSettings() {
        val waterGoal = preferencesManager.getWaterGoal()
        val reminderEnabled = preferencesManager.isReminderEnabled()
        val reminderInterval = preferencesManager.getReminderInterval()
        val userName = authManager.getUserName()
        val userEmail = authManager.getUserEmail()

        val intervalText = when (reminderInterval) {
            30 -> "Every 30 minutes"
            60 -> "Every 1 hour"
            120 -> "Every 2 hours"
            180 -> "Every 3 hours"
            else -> "Every 1 hour"
        }

        binding.currentSettingsText.text = """
            Account Information:

            👤 Name: $userName
            📧 Email: $userEmail

            Current Settings:
            
            💧 Daily Water Goal: $waterGoal glasses
            ⏰ Reminders: ${if (reminderEnabled) "Enabled" else "Disabled"}
            ⏱️ Reminder Interval: $intervalText
            
            📱 App Version: ${getString(R.string.app_version)}
        """.trimIndent()
    }

    private fun showResetConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.reset_all_data))
            .setMessage(getString(R.string.confirm_reset))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                resetAllData()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun resetAllData() {
        preferencesManager.clearAllData()
        displayCurrentSettings()
        Snackbar.make(binding.root, "All data has been reset", Snackbar.LENGTH_LONG).show()
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.logout))
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                logout()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun logout() {
        authManager.logout()
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
