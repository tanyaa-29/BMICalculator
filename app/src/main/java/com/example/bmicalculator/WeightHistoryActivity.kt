package com.example.bmicalculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmicalculator.auth.AuthManager
import com.example.bmicalculator.auth.WeightHistory
import com.example.bmicalculator.databinding.ActivityWeightHistoryBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeightHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeightHistoryBinding
    private lateinit var authManager: AuthManager
    private lateinit var adapter: WeightHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeightHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authManager = AuthManager(this)
        setupRecyclerView()
        loadHistory()
    }

    private fun setupRecyclerView() {
        adapter = WeightHistoryAdapter(emptyList())
        binding.rvWeightHistory.layoutManager = LinearLayoutManager(this)
        binding.rvWeightHistory.adapter = adapter
    }

    private fun loadHistory() {
        lifecycleScope.launch {
            val user = authManager.db.getUserByEmail(authManager.getLoggedInEmail())
            if (user != null) {
                val history = authManager.db.getWeightHistory(user.id)
                adapter.updateData(history)
            }
        }
    }

    class WeightHistoryAdapter(private var list: List<WeightHistory>) : RecyclerView.Adapter<WeightHistoryAdapter.ViewHolder>() {
        
        fun updateData(newList: List<WeightHistory>) {
            list = newList
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            holder.text1.text = "${item.weightKg} kg"
            holder.text2.text = sdf.format(Date(item.timestamp))
        }

        override fun getItemCount() = list.size

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val text1: TextView = view.findViewById(android.R.id.text1)
            val text2: TextView = view.findViewById(android.R.id.text2)
            init {
                text1.setTextColor(android.graphics.Color.WHITE)
                text2.setTextColor(android.graphics.Color.GRAY)
            }
        }
    }
}
