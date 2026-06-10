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
import com.example.wellnesstracker.databinding.FragmentLoginBinding
import com.example.wellnesstracker.utils.AuthManager
import com.google.android.material.snackbar.Snackbar

/**
 * Login Fragment - Handles user authentication
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var authManager: AuthManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = AuthManager(requireContext())
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        binding.tvSignupLink.setOnClickListener {
            navigateToSignUp()
        }
    }

    private fun handleLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Clear previous errors
        binding.tilEmail.error = null
        binding.tilPassword.error = null

        // Validate inputs
        if (!validateEmail(email)) {
            binding.tilEmail.error = "Please enter a valid email"
            return
        }

        if (!validatePassword(password)) {
            binding.tilPassword.error = "Password must be at least 6 characters"
            return
        }

        // Check if user exists
        if (!authManager.userExists(email)) {
            Snackbar.make(
                binding.root,
                "No account found with this email. Please sign up first.",
                Snackbar.LENGTH_LONG
            ).show()
            return
        }

        // Attempt login
        if (authManager.login(email, password)) {
            val userName = authManager.getUserName()
            Snackbar.make(binding.root, "Welcome back, $userName! 🌟", Snackbar.LENGTH_SHORT).show()
            navigateToMainActivity()
        } else {
            Snackbar.make(
                binding.root,
                "Invalid password. Please try again.",
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

    private fun navigateToSignUp() {
        val signUpFragment = SignUpFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.auth_container, signUpFragment)
            .addToBackStack(null)
            .commit()
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
