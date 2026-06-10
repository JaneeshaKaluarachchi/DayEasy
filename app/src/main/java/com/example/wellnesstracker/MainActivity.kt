package com.example.wellnesstracker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.wellnesstracker.databinding.ActivityMainBinding
import com.example.wellnesstracker.fragments.AnalyticsFragment
import com.example.wellnesstracker.fragments.HabitsFragment
import com.example.wellnesstracker.fragments.HydrationFragment
import com.example.wellnesstracker.fragments.MoodFragment
import com.example.wellnesstracker.fragments.SettingsFragment
import com.example.wellnesstracker.utils.NotificationHelper
import com.example.wellnesstracker.utils.PreferencesManager


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesManager = PreferencesManager(this)

        // Set up initial fragment
        if (savedInstanceState == null) {
            // Check if opened from notification
            handleNotificationIntent()
        }

        setupBottomNavigation()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent()
    }

    /**
     * Handle navigation from notification
     */
    private fun handleNotificationIntent() {
        val shouldNavigateToHydration = intent?.getBooleanExtra(
            NotificationHelper.EXTRA_NAVIGATE_TO_HYDRATION,
            false
        ) ?: false

        val markAsDone = intent?.getBooleanExtra("mark_as_done", false) ?: false

        if (shouldNavigateToHydration) {
            // Navigate to hydration tab
            binding.bottomNavigation.selectedItemId = R.id.nav_hydration
            loadFragment(HydrationFragment())

            // If mark as done was clicked, increment water count
            if (markAsDone) {
                preferencesManager.incrementWaterCount()
            }
        } else {
            // Default to habits fragment
            loadFragment(HabitsFragment())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.nav_habits -> HabitsFragment()
                R.id.nav_mood -> MoodFragment()
                R.id.nav_hydration -> HydrationFragment()
                R.id.nav_analytics -> AnalyticsFragment()
                R.id.nav_settings -> SettingsFragment()
                else -> HabitsFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
