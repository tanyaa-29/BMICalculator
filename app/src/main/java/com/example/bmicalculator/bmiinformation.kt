package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView

class bmiinformation : AppCompatActivity() {
    private var f = 0
    private var selected1: String? = null
    private var gender: String? = null
    private var selectedAge: String? = null
    private var kg: String? = null
    private var pounds: String? = null
    private var cm: String? = null
    private var inch: String? = null
    private var feet: String? = null

    private var age: TextView? = null
    private var height: TextView? = null
    private var weight: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiinformation)

        // Get intent extras
        intent.extras?.let {
            selected1 = it.getString("selected1")
            gender = it.getString("gender")
            selectedAge = it.getString("selectedAge")
            kg = it.getString("kg")
            pounds = it.getString("pounds")
            cm = it.getString("cm")
            inch = it.getString("inch")
            feet = it.getString("feet")
        }

        // Lottie animation
        val lottieAnimation = findViewById<LottieAnimationView>(R.id.lottieAnimation)
        lottieAnimation.playAnimation()
        lottieAnimation.loop(true)

        // TextViews
        age = findViewById(R.id.age)
        height = findViewById(R.id.height)
        weight = findViewById(R.id.weight)

        age?.text = selectedAge

        if (selected1 == "KG") {
            height?.text = cm
            weight?.text = "Kg: $kg"
        } else {
            height?.text = "Feet: $feet\nInch: $inch"
            weight?.text = "Pounds: $pounds"
        }

        // CardViews
        val calbmi = findViewById<TextView>(R.id.calbmi)
        val agecard = findViewById<CardView>(R.id.agecard)
        val heightcard = findViewById<CardView>(R.id.heightcard)
        val weightcard = findViewById<CardView>(R.id.weightcard)

        // Animations
        val toptodown = AnimationUtils.loadAnimation(this, R.anim.slidefromtoptobottom)
        toptodown.duration = 2000
        lottieAnimation.startAnimation(toptodown)
        calbmi.startAnimation(toptodown)

        val lefttoright = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        lefttoright.duration = 2000
        agecard.startAnimation(lefttoright)

        val righttoleft = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        righttoleft.duration = 2000
        heightcard.startAnimation(righttoleft)
        weightcard.startAnimation(righttoleft)

        // Move to bmiresult after 4 seconds
        Handler().postDelayed({
            val intent1 = Intent(this, bmiresult::class.java)
            intent1.putExtra("selected1", selected1)
            intent1.putExtra("selectedAge", selectedAge)
            intent1.putExtra("kg", kg)
            intent1.putExtra("pounds", pounds)
            intent1.putExtra("gender", gender)
            intent1.putExtra("cm", cm)
            intent1.putExtra("inch", inch)
            intent1.putExtra("feet", feet)
            startActivity(intent1)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            f = 1
            finish()
        }, 4000)
    }

    override fun finish() {
        super.finish()
        if (f == 0) {
            // If the user leaves activity before Handler triggers, use reverse slide animation
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}
