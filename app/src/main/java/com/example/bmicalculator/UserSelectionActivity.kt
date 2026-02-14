package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bmicalculator.auth.AuthManager
import com.example.bmicalculator.auth.RegisterActivity
import com.example.bmicalculator.databinding.ActivityUserSelectionBinding
import kotlinx.coroutines.launch

class UserSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserSelectionBinding
    private lateinit var authManager: AuthManager
    private lateinit var adapter: UserProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authManager = AuthManager(this)
        
        setupRecyclerView()
        
        binding.btnAddProfile.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        loadUsers()
    }

    private fun setupRecyclerView() {
        adapter = UserProfileAdapter(emptyList()) { user ->
            authManager.saveSession(user)
            startActivity(Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }
        binding.rvUserProfiles.layoutManager = LinearLayoutManager(this)
        binding.rvUserProfiles.adapter = adapter
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            val users = authManager.db.getAllUsers()
            adapter.updateData(users)
        }
    }

    override fun onResume() {
        super.onResume()
        loadUsers()
    }
}
