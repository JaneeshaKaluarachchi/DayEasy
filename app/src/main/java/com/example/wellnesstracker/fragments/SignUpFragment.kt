package com.example.wellnesstracker.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wellnesstracker.MainActivity
import com.example.wellnesstracker.R
import com.example.wellnesstracker.databinding.FragmentSignupBinding
import com.example.wellnesstracker.utils.AuthManager
import com.google.android.material.snackbar.Snackbar

/**
 * Sign Up Fragment - Handles user registration
 */
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext())
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnSignup.setOnClickListener {
            handleSignUp()
        }

        binding.tvLoginLink.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun handleSignUp() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        // Clear previous errors
        binding.tilName.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmPassword.error = null

        // Validate inputs
        if (name.isEmpty()) {
            binding.tilName.error = "Please enter your full name"
            return
        }

        if (!validateEmail(email)) {
            binding.tilEmail.error = "Please enter a valid email"
            return
        }

        if (!validatePassword(password)) {
            binding.tilPassword.error = "Password must be at least 6 characters"
            return
        }

        if (password != confirmPassword) {
            binding.tilConfirmPassword.error = "Passwords do not match"
            return
        }

        // Check if user already exists
        if (authManager.userExists(email)) {
            binding.tilEmail.error = "This email is already registered. Please login."
            Snackbar.make(
                binding.root,
                "Account already exists. Please use the login page.",
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        // Attempt sign up
        if (authManager.signUp(name, email, password)) {
            Snackbar.make(binding.root, "Account created successfully! Welcome, $name! 🎉", Snackbar.LENGTH_SHORT).show()
            navigateToMainActivity()
        } else {
            Snackbar.make(
                binding.root,
                "Failed to create account. Please try again.",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun validateEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validatePassword(password: String): Boolean {
        return password.length >= 6
    }

    private fun navigateToLogin() {
        parentFragmentManager.popBackStack()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
