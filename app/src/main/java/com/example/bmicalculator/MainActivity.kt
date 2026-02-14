package com.example.bmicalculator

import com.example.bmicalculator.auth.AuthManager
import com.example.bmicalculator.auth.LoginActivity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.bmicalculator.auth.WeightHistory
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    var selected1: String? = null
    var selectedAge: Int? = null
    var selectedWeight: Int? = null
    var ageList: MutableList<Int> = ArrayList()
    var weightList: MutableList<Int> = ArrayList()
    var genderselect: String = ""
    var agecounter: TextView? = null
    var weightcounter: TextView? = null
    
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authManager = AuthManager(this)

        val btnLogout: Button = findViewById(R.id.btnLogout)
        btnLogout.setOnClickListener {
            authManager.logout()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Add Profile Switching button to title or menu
        findViewById<View>(R.id.btnSwitchProfile).setOnClickListener {
            startActivity(Intent(this, UserSelectionActivity::class.java))
        }

        val agerecyclerview = findViewById<RecyclerView>(R.id.agerecyclerview)
        val weightrecyclerview = findViewById<RecyclerView>(R.id.weightrecyclerview)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        agerecyclerview.layoutManager = layoutManager
        weightrecyclerview.layoutManager = layoutManager1

        LinearSnapHelper().attachToRecyclerView(agerecyclerview)
        LinearSnapHelper().attachToRecyclerView(weightrecyclerview)

        for (i in 1..149) ageList.add(i)
        for (i in 1..199) weightList.add(i)

        val ageRCadapter = ageRCadapter(ageList)
        val weightRCadapter = ageRCadapter(weightList)

        agerecyclerview.adapter = ageRCadapter
        weightrecyclerview.adapter = weightRCadapter

        Handler(Looper.getMainLooper()).postDelayed({
            agerecyclerview.scrollToPosition(0)
            weightrecyclerview.scrollToPosition(0)
        }, 300)

        agerecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateCenteredPosition(agerecyclerview, ageRCadapter)
                }
            }
        })

        weightrecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateCenteredPositionw(weightrecyclerview, weightRCadapter)
                }
            }
        })

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

        val kg = findViewById<MaterialButton>(R.id.kg)
        val pounds = findViewById<MaterialButton>(R.id.pounds)

        kg.setOnClickListener {
            selected1 = "KG"
            kg.setBackgroundColor(Color.BLACK)
            pounds.setBackgroundColor("#3F51B5".toColorInt())
            updateWeightDisplay()
        }

        pounds.setOnClickListener {
            selected1 = "Pounds"
            pounds.setBackgroundColor(Color.BLACK)
            kg.setBackgroundColor("#3F51B5".toColorInt())
            updateWeightDisplay()
        }

        agecounter = findViewById(R.id.agecounter)
        weightcounter = findViewById(R.id.weightcounter)

        ageRCadapter.setOnItemClickListener(object : ageRCadapter.OnItemClickListener {
            override fun onItemClick(position: Int, age: Int) {
                selectedAge = ageList[position]
                agecounter?.text = "Age: $selectedAge"
            }
        })

        weightRCadapter.setOnItemClickListener(object : ageRCadapter.OnItemClickListener {
            override fun onItemClick(position: Int, age: Int) {
                selectedWeight = weightList[position]
                updateWeightDisplay()
            }
        })

        val nextheight = findViewById<MaterialButton>(R.id.nextheight)
        nextheight.setOnClickListener {
            if (selected1 != null && selectedAge != null && selectedWeight != null && genderselect.isNotEmpty()) {
                
                // Save Weight to History
                saveWeightToHistory()

                val intent = Intent(this, heightselect::class.java)
                intent.putExtra("selected1", selected1)
                intent.putExtra("selectedAge", selectedAge.toString())
                intent.putExtra("selectedWeight", selectedWeight.toString())
                intent.putExtra("gender", genderselect)
                startActivity(intent)
                @Suppress("DEPRECATION")
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } else {
                Toast.makeText(this, "Check All The Fields", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Load Graph Button
        findViewById<View>(R.id.btnShowHistory).setOnClickListener {
             startActivity(Intent(this, WeightHistoryActivity::class.java))
        }
    }

    private fun updateWeightDisplay() {
        val unit = if (selected1 == "KG") "KG" else "Pounds"
        weightcounter?.text = "Weight: ${selectedWeight ?: ""} $unit"
    }

    private fun saveWeightToHistory() {
        val currentEmail = authManager.getLoggedInEmail()
        val weight = selectedWeight?.toFloat() ?: return
        
        lifecycleScope.launch {
            val user = authManager.db.getUserByEmail(currentEmail)
            if (user != null) {
                authManager.db.insertWeight(WeightHistory(userId = user.id, weightKg = weight))
            }
        }
    }

    private fun updateCenteredPosition(recyclerView: RecyclerView, adapter: ageRCadapter) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val center = recyclerView.width / 2
        var minDistance = Int.MAX_VALUE
        var closestChild: View? = null
        var closestPosition = RecyclerView.NO_POSITION

        for (i in 0 until layoutManager.childCount) {
            val child = layoutManager.getChildAt(i)
            val childCenter = (layoutManager.getDecoratedLeft(child!!) + layoutManager.getDecoratedRight(child)) / 2
            val distance = Math.abs(childCenter - center)
            if (distance < minDistance) {
                minDistance = distance
                closestChild = child
            }
        }

        if (closestChild != null) {
            closestPosition = layoutManager.getPosition(closestChild)
            if (closestPosition in 0 until adapter.itemCount) {
                selectedAge = ageList[closestPosition]
                agecounter?.text = "Age: $selectedAge"
                adapter.setSelectedItem(closestPosition)
            }
        }
    }


    private fun updateCenteredPositionw(recyclerView: RecyclerView, adapter: ageRCadapter) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val center = recyclerView.width / 2
        var minDistance = Int.MAX_VALUE
        var closestChild: View? = null
        var closestPosition = RecyclerView.NO_POSITION

        for (i in 0 until layoutManager.childCount) {
            val child = layoutManager.getChildAt(i)
            val childCenter = (layoutManager.getDecoratedLeft(child!!) + layoutManager.getDecoratedRight(child)) / 2
            val distance = Math.abs(childCenter - center)
            if (distance < minDistance) {
                minDistance = distance
                closestChild = child
            }
        }

        if (closestChild != null) {
            closestPosition = layoutManager.getPosition(closestChild)
            if (closestPosition in 0 until adapter.itemCount) {
                selectedWeight = weightList[closestPosition]
                updateWeightDisplay()
                adapter.setSelectedItem(closestPosition)
            }
        }
    }
}
