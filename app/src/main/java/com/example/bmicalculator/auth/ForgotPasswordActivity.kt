package com.example.bmicalculator.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bmicalculator.databinding.ForgotPasswordBinding
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────
// FILE: auth/ForgotPasswordActivity.kt
// Reset password using secret question answer
// ─────────────────────────────────────────────────

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ForgotPasswordBinding
    private lateinit var authManager: AuthManager
    private var foundUser: UserEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Reset Password"

        authManager = AuthManager(this)

        // Step 1: Find account
        binding.btnFindAccount.setOnClickListener {
            val email = binding.etEmail.text.toString().trim().lowercase()
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.etEmail.error = "Enter valid email"; return@setOnClickListener
            }
            showLoading(true)
            lifecycleScope.launch {
                val user = authManager.db.getUserByEmail(email)
                runOnUiThread {
                    showLoading(false)
                    if (user == null) {
                        Toast.makeText(this@ForgotPasswordActivity, "No account found with this email", Toast.LENGTH_SHORT).show()
                    } else {
                        foundUser = user
                        // Show secret question
                        binding.tvSecretQuestion.text = user.secretQuestion
                        binding.layoutStep2.visibility = View.VISIBLE
                        binding.btnFindAccount.isEnabled = false
                        binding.etEmail.isEnabled = false
                    }
                }
            }
        }

        // Step 2: Verify answer & reset password
        binding.btnResetPassword.setOnClickListener {
            val answer = binding.etSecretAnswer.text.toString().trim().lowercase()
            val newPass = binding.etNewPassword.text.toString()
            val confirmPass = binding.etConfirmNewPassword.text.toString()

            if (answer.isEmpty()) { binding.etSecretAnswer.error = "Enter your answer"; return@setOnClickListener }
            if (newPass.isEmpty() || newPass.length < 6) { binding.etNewPassword.error = "Min 6 characters"; return@setOnClickListener }
            if (!newPass.any { it.isDigit() }) { binding.etNewPassword.error = "Must contain a number"; return@setOnClickListener }
            if (newPass != confirmPass) { binding.etConfirmNewPassword.error = "Passwords don't match"; return@setOnClickListener }

            val user = foundUser ?: return@setOnClickListener
            val hashedAnswer = AuthManager.hashPassword(answer)

            if (hashedAnswer != user.secretAnswer) {
                binding.etSecretAnswer.error = "Incorrect answer"
                return@setOnClickListener
            }

            showLoading(true)
            lifecycleScope.launch {
                authManager.db.updatePassword(user.email, AuthManager.hashPassword(newPass))
                runOnUiThread {
                    showLoading(false)
                    Toast.makeText(this@ForgotPasswordActivity, "Password reset successfully! Please login.", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
