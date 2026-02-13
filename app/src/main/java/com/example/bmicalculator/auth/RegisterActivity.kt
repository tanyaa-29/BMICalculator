package com.example.bmicalculator.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bmicalculator.databinding.ActivityRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Create Account"

        authManager = AuthManager(this)

        val questions = arrayOf(
            "What was the name of your first pet?",
            "What is your mother's maiden name?",
            "What city were you born in?",
            "What was your childhood nickname?",
            "What is the name of your favorite teacher?"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, questions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerQuestion.adapter = adapter

        binding.btnRegister.setOnClickListener { register() }
        binding.tvLogin.setOnClickListener { finish() }
    }

    private fun register() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim().lowercase()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        val secretAnswer = binding.etSecretAnswer.text.toString().trim().lowercase()

        if (name.isEmpty()) { binding.etName.error = "Enter your name"; return }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Valid email required"; return
        }
        if (password.length < 6 || !password.any { it.isDigit() }) {
            binding.etPassword.error = "Min 6 chars + 1 number"; return
        }
        if (confirmPassword != password) {
            binding.etConfirmPassword.error = "Passwords match error"; return
        }
        if (secretAnswer.isEmpty()) { binding.etSecretAnswer.error = "Answer required"; return }

        val selectedQuestion = binding.spinnerQuestion.selectedItem.toString()

        showLoading(true)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val exists = authManager.db.emailExists(email) > 0
                if (exists) {
                    withContext(Dispatchers.Main) {
                        showLoading(false)
                        binding.etEmail.error = "Email already registered"
                    }
                    return@launch
                }

                val user = UserEntity(
                    name = name,
                    email = email,
                    passwordHash = AuthManager.hashPassword(password),
                    secretQuestion = selectedQuestion,
                    secretAnswer = AuthManager.hashPassword(secretAnswer)
                )
                
                val id = authManager.db.insertUser(user)
                
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    if (id > 0) {
                        Toast.makeText(this@RegisterActivity, "Registration Successful!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        })
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showLoading(false)
                    Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !show
    }

    override fun onSupportNavigateUp(): Boolean { finish(); return true }
}
