package com.example.wellnesstracker.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Authentication manager for handling user login/signup and session management
 */
class AuthManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "VitalFlowAuthPrefs"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_CURRENT_USER_EMAIL = "current_user_email"
        private const val KEY_USER_PREFIX = "user_"
        private const val KEY_NAME_SUFFIX = "_name"
        private const val KEY_PASSWORD_SUFFIX = "_password"
    }

    /**
     * Sign up a new user
     */
    fun signUp(name: String, email: String, password: String): Boolean {
        return try {
            // Check if user already exists
            if (userExists(email)) {
                return false
            }

            // Store user credentials with email as key
            sharedPreferences.edit().apply {
                putString(KEY_USER_PREFIX + email + KEY_NAME_SUFFIX, name)
                putString(KEY_USER_PREFIX + email + KEY_PASSWORD_SUFFIX, password)
                putBoolean(KEY_IS_LOGGED_IN, true)
                putString(KEY_CURRENT_USER_EMAIL, email)
                apply()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Login an existing user
     */
    fun login(email: String, password: String): Boolean {
        val storedPassword = sharedPreferences.getString(KEY_USER_PREFIX + email + KEY_PASSWORD_SUFFIX, null)

        return if (password == storedPassword && storedPassword != null) {
            sharedPreferences.edit().apply {
                putBoolean(KEY_IS_LOGGED_IN, true)
                putString(KEY_CURRENT_USER_EMAIL, email)
                apply()
            }
            true
        } else {
            false
        }
    }

    /**
     * Logout the current user
     */
    fun logout() {
        sharedPreferences.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, false)
            remove(KEY_CURRENT_USER_EMAIL)
            apply()
        }
    }

    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Check if user exists (has signed up before)
     */
    fun userExists(email: String): Boolean {
        return sharedPreferences.getString(KEY_USER_PREFIX + email + KEY_PASSWORD_SUFFIX, null) != null
    }

    /**
     * Get the current logged-in user's name
     */
    fun getUserName(): String {
        val email = getCurrentUserEmail()
        return sharedPreferences.getString(KEY_USER_PREFIX + email + KEY_NAME_SUFFIX, "") ?: ""
    }

    /**
     * Get the current logged-in user's email
     */
    fun getUserEmail(): String {
        return getCurrentUserEmail()
    }

    /**
     * Get the current user's email (used internally)
     */
    fun getCurrentUserEmail(): String {
        return sharedPreferences.getString(KEY_CURRENT_USER_EMAIL, "") ?: ""
    }
}
