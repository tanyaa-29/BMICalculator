package com.example.bmicalculator.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bmicalculator.MainActivity
import com.example.bmicalculator.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────
// FILE: auth/LoginActivity.kt
// ─────────────────────────────────────────────────

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authManager = AuthManager(this)

        // Check if already logged in
        if (authManager.isLoggedIn()) {
            startMainActivity()
        }

        binding.btnLogin.setOnClickListener { login() }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim().lowercase()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty()) { binding.etEmail.error = "Enter email"; return }
        if (password.isEmpty()) { binding.etPassword.error = "Enter password"; return }

        showLoading(true)
        lifecycleScope.launch {
            val user = authManager.db.login(email, AuthManager.hashPassword(password))
            runOnUiThread {
                showLoading(false)
                if (user != null) {
                    authManager.saveSession(user)
                    startMainActivity()
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !show
    }
}
