package com.example.bmicalculator

import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ageRCadapter(private val ageList: List<Int>) :
    RecyclerView.Adapter<ageRCadapter.AgeViewHolder>() {

    private var selectedItem = RecyclerView.NO_POSITION

    interface OnItemClickListener {
        fun onItemClick(position: Int, age: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.agescroll, parent, false)
        return AgeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgeViewHolder, position: Int) {
        val age = ageList[position]
        holder.textAge.text = age.toString()

        if (position == selectedItem) {
            // Highlight the selected item
            holder.textAge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40f)
            holder.textAge.setBackgroundColor(Color.BLUE)
            holder.textAge.setTextColor(Color.WHITE)
        } else {
            // Reset style for unselected items
            holder.textAge.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            holder.textAge.setBackgroundColor(Color.TRANSPARENT)
            holder.textAge.setTextColor(Color.WHITE)
        }
    }

    override fun getItemCount(): Int = ageList.size

    // Update selected position
    fun setSelectedItem(position: Int) {
        selectedItem = position
        notifyDataSetChanged()
    }

    inner class AgeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textAge: TextView = itemView.findViewById(R.id.textAge)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    setSelectedItem(position) // highlight clicked item
                    listener?.onItemClick(position, ageList[position])
                }
            }
        }
    }
}
