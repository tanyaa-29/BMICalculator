package com.example.bmicalculator

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    var selected1: String? = null
    var selectedAge: Int? = null
    var selectedWeight: Int? = null
    var ageList: MutableList<Int> = ArrayList()
    var weightList: MutableList<Int> = ArrayList()
    var genderselect: String = ""
    var agecounter: TextView? = null
    var weightcounter: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val agerecyclerview = findViewById<RecyclerView>(R.id.agerecyclerview)
        val weightrecyclerview = findViewById<RecyclerView>(R.id.weightrecyclerview)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        agerecyclerview.layoutManager = layoutManager
        weightrecyclerview.layoutManager = layoutManager1

        // Initialize lists starting from 1
        for (i in 1..149) ageList.add(i)
        for (i in 1..199) weightList.add(i)

        // Setup adapters
        val ageRCadapter = ageRCadapter(ageList)
        val weightRCadapter = ageRCadapter(weightList)

        agerecyclerview.adapter = ageRCadapter
        weightrecyclerview.adapter = weightRCadapter

        // Scroll to first element
        Handler(Looper.getMainLooper()).postDelayed({
            agerecyclerview.scrollToPosition(0)
            weightrecyclerview.scrollToPosition(0)
        }, 300)

        // RecyclerView scroll listeners
        agerecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val centerPosition = layoutManager.findFirstVisibleItemPosition() + layoutManager.childCount / 2
                ageRCadapter.setSelectedItem(centerPosition)
                updateCenteredPosition(agerecyclerview, ageRCadapter)
            }
        })

        weightrecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val centerPosition = layoutManager1.findFirstVisibleItemPosition() + layoutManager1.childCount / 2
                weightRCadapter.setSelectedItem(centerPosition)
                updateCenteredPositionw(weightrecyclerview, weightRCadapter)
            }
        })

        // Gender selection
        val male: ImageButton = findViewById(R.id.male)
        val female: ImageButton = findViewById(R.id.female)
        val maleselect: ImageView = findViewById(R.id.maleselect)
        val femaleselect: ImageView = findViewById(R.id.femaleselect)
        maleselect.visibility = View.INVISIBLE
        femaleselect.visibility = View.INVISIBLE

        male.setOnClickListener {
            maleselect.visibility = View.VISIBLE
            femaleselect.visibility = View.INVISIBLE
            genderselect = "male"
        }

        female.setOnClickListener {
            femaleselect.visibility = View.VISIBLE
            maleselect.visibility = View.INVISIBLE
            genderselect = "female"
        }

        // KG / Pounds selection
        val kg = findViewById<MaterialButton>(R.id.kg)
        val pounds = findViewById<MaterialButton>(R.id.pounds)

        kg.setOnClickListener {
            selected1 = "KG"
            kg.setBackgroundColor(Color.BLACK)
            pounds.setBackgroundColor("#3F51B5".toColorInt())
        }

        pounds.setOnClickListener {
            selected1 = "Pounds"
            pounds.setBackgroundColor(Color.BLACK)
            kg.setBackgroundColor("#3F51B5".toColorInt())
        }

        // Counters
        agecounter = findViewById(R.id.agecounter)
        weightcounter = findViewById(R.id.weightcounter)

        // Item click listeners
        ageRCadapter.setOnItemClickListener(object : ageRCadapter.OnItemClickListener {
            override fun onItemClick(position: Int, age: Int) {
                selectedAge = ageList[position]
                agecounter?.text = selectedAge.toString()
            }
        })

        weightRCadapter.setOnItemClickListener(object : ageRCadapter.OnItemClickListener {
            override fun onItemClick(position: Int, age: Int) {
                selectedWeight = weightList[position]
                weightcounter?.text = selectedWeight.toString()
            }
        })

        // NEXT button
        val nextheight = findViewById<MaterialButton>(R.id.nextheight)
        nextheight.setOnClickListener {
            if (selected1 != null && selectedAge != null && selectedWeight != null && genderselect.isNotEmpty()) {
                val intent = Intent(this, heightselect::class.java)
                intent.putExtra("selected1", selected1)
                intent.putExtra("selectedAge", selectedAge.toString())
                intent.putExtra("selectedWeight", selectedWeight.toString())
                intent.putExtra("gender", genderselect)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } else {
                Toast.makeText(this, "Check All The Fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateCenteredPosition(recyclerView: RecyclerView, adapter: ageRCadapter) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisible = layoutManager.findFirstVisibleItemPosition()
        val lastVisible = layoutManager.findLastVisibleItemPosition()
        val centerPosition = (firstVisible + lastVisible) / 2
        if (centerPosition in 0 until adapter.itemCount) {
            selectedAge = ageList[centerPosition]
            agecounter?.text = selectedAge.toString()
        }
    }

    private fun updateCenteredPositionw(recyclerView: RecyclerView, adapter: ageRCadapter) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisible = layoutManager.findFirstVisibleItemPosition()
        val lastVisible = layoutManager.findLastVisibleItemPosition()
        val centerPosition = (firstVisible + lastVisible) / 2
        if (centerPosition in 0 until adapter.itemCount) {
            selectedWeight = weightList[centerPosition]
            weightcounter?.text = selectedWeight.toString()
        }
    }
}






