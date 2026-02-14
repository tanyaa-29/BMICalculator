package com.example.bmicalculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bmicalculator.auth.UserEntity

class UserProfileAdapter(
    private var users: List<UserEntity>,
    private val onUserClick: (UserEntity) -> Unit
) : RecyclerView.Adapter<UserProfileAdapter.ViewHolder>() {

    fun updateData(newUsers: List<UserEntity>) {
        users = newUsers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.name.text = user.name
        holder.email.text = user.email
        holder.itemView.setOnClickListener { onUserClick(user) }
    }

    override fun getItemCount() = users.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(android.R.id.text1)
        val email: TextView = view.findViewById(android.R.id.text2)

        init {
            name.setTextColor(android.graphics.Color.WHITE)
            email.setTextColor(android.graphics.Color.GRAY)
        }
    }
}
