package com.example.bmicalculator
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import kotlin.math.pow

class bmiresult : AppCompatActivity() {
    var cm: Int = 0
    var feet: Int = 0
    var inch: Int = 0
    var kg: Int = 0
    var pounds: Int = 0
    var weightbetween: Int = 0
    var bmicount: Float = 0f
    var neededweightfrom: Float = 0f
    var neededweightto: Float = 0f
    var bmi: TextView? = null
    var needweight: TextView? = null
    var bmiinfo: TextView? = null
    var bmicard: MaterialCardView? = null
    var neededweightcard: MaterialCardView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiresult)

        bmi = findViewById<TextView?>(R.id.bmi)
        needweight = findViewById<TextView?>(R.id.neededweight)
        bmiinfo = findViewById<TextView?>(R.id.bmiinfo)
        bmicard = findViewById<MaterialCardView?>(R.id.bmicard)
        neededweightcard = findViewById<MaterialCardView?>(R.id.neededweightcard)

        val intent = getIntent()

        if (intent.getStringExtra("cm") != null) {
            cm = intent.getStringExtra("cm")!!.toInt()
        }
        if (intent.getStringExtra("feet") != null) {
            feet = intent.getStringExtra("feet")!!.toInt()
        }
        if (intent.getStringExtra("inch") != null) {
            inch = intent.getStringExtra("inch")!!.toInt()
        }
        if (intent.getStringExtra("kg") != null) {
            kg = intent.getStringExtra("kg")!!.toInt()
        }
        if (intent.getStringExtra("pounds") != null) {
            pounds = intent.getStringExtra("pounds")!!.toInt()
        }
        if (intent.getStringExtra("weightbetween") != null) {
            weightbetween = intent.getStringExtra("weightbetween")!!.toInt()
        }


        println(cm)
        println(feet)
        println(inch)
        println(kg)
        println(pounds)
        println(weightbetween)
        println()

        if (cm == 0) {
            val feettoInches = ((feet * 12) + inch).toDouble()
            val inchtocm = (feettoInches * 2.54).toFloat()
            bmicount = (pounds / (inchtocm / 100.0).pow(2.0)).toFloat()
            bmi!!.setText(bmicount.toString() + "")
        } else {
            bmicount = (kg / (cm / 100.0).pow(2.0)).toFloat()
            bmi!!.setText(bmicount.toString() + "")
        }

        if (bmicount < 18.5) {
            bmiinfo!!.setText("UnderWeight")
            bmiinfo!!.setTextColor(Color.rgb(255, 193, 7))
            bmi!!.setTextColor(Color.rgb(255, 193, 7))
            bmicard!!.setStrokeColor(Color.rgb(255, 193, 7))
            neededweightcard!!.setStrokeColor(Color.rgb(255, 193, 7))
        } else if (bmicount > 25.0) {
            bmiinfo!!.setText("OverWeight")
            bmiinfo!!.setTextColor(Color.rgb(255, 0, 0))
            bmi!!.setTextColor(Color.rgb(255, 0, 0))
            bmicard!!.setStrokeColor(Color.rgb(255, 0, 0))
            neededweightcard!!.setStrokeColor(Color.rgb(255, 0, 0))
        } else {
            bmiinfo!!.setText("Normal")
            bmiinfo!!.setTextColor(Color.rgb(255, 193, 7))
            bmi!!.setTextColor(Color.rgb(255, 193, 7))
            bmicard!!.setStrokeColor(Color.rgb(255, 193, 7))
            neededweightcard!!.setStrokeColor(Color.rgb(255, 193, 7))
        }
        if (cm == 0) {
            val feettoInches = ((feet * 12) + inch).toDouble()
            val inchtocm = (feettoInches * 2.54).toFloat()
            neededweightfrom = (18.1 * (inchtocm / 100.0).pow(2.0)).toFloat()
            neededweightto = (25.0 * (inchtocm / 100.0).pow(2.0)).toFloat()
            needweight!!.setText(neededweightfrom.toInt().toString() + "~" + neededweightto.toInt())
        } else {
            neededweightfrom = (18.1 * (cm / 100.0).pow(2.0)).toFloat()
            neededweightto = (25.0 * (cm / 100.0).pow(2.0)).toFloat()
            needweight!!.setText(neededweightfrom.toInt().toString() + "~" + neededweightto.toInt())
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}