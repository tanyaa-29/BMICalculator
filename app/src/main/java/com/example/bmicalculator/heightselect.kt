package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class heightselect : AppCompatActivity() {
    var selected1: String? = null
    var gender: String? = null
    var selectedAge: Int? = null
    var selectedWeight: Int? = null
    var pounds: Int? = null
    var kg: Int? = null
    var cm: Int? = null
    var inch: Int? = null
    var feet: Int? = null
    var inchlist: MutableList<Int?>? = null
    var feetList: MutableList<Int?>? = null
    var weightList: MutableList<Int?>? = null
    var selectedinch: TextView? = null
    var selectedfeet: TextView? = null
    var selectedcm: TextView? = null
    var f: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heightselect)

        selectedinch = findViewById<TextView?>(R.id.selectedinch)
        selectedfeet = findViewById<TextView?>(R.id.selectedfeet)
        selectedcm = findViewById<TextView?>(R.id.selectedcm)

        val intent = getIntent()
        selected1 = intent.getStringExtra("selected1")
        selectedAge = intent.getStringExtra("selectedAge")!!.toInt()
        selectedWeight = intent.getStringExtra("selectedWeight")!!.toInt()
        gender = intent.getStringExtra("gender")

        val avatar = findViewById<ImageView?>(R.id.avatar)
        if (gender == "male") {
            avatar.setImageResource(R.drawable.standingmale)
        } else {
            avatar.setImageResource(R.drawable.standingfemale)
        }

        val llcm = findViewById<LinearLayout?>(R.id.llcm)
        val llft = findViewById<LinearLayout?>(R.id.llft)

        if (selected1 == "KG") {
            llcm.setVisibility(View.VISIBLE)
            llft.setVisibility(View.GONE)
            kg = selectedWeight
        } else {
            llcm.setVisibility(View.GONE)
            llft.setVisibility(View.VISIBLE)
            pounds = selectedWeight
        }
        val rcheight = findViewById<RecyclerView?>(R.id.rcheight)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rcheight.setLayoutManager(layoutManager)


        weightList = ArrayList<Int?>()

        for (i in 1..300) {
            weightList!!.add(i)
        }

        val ageRCadapter = ageRCadapter(weightList as List<Int>)
        rcheight.setAdapter(ageRCadapter)


        var centerPosition = weightList!!.size / 2

        rcheight.scrollToPosition(centerPosition)

        rcheight.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val centerposition =
                    layoutManager.findFirstVisibleItemPosition() + layoutManager.getChildCount() / 2
                ageRCadapter.setSelectedItem(centerposition)
                updateCenteredPositioncm(rcheight, ageRCadapter)
            }
        })


        val rpfeet = findViewById<RecyclerView?>(R.id.rpfeet)

        val layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rpfeet.setLayoutManager(layoutManager1)


        feetList = ArrayList<Int?>()

        for (i in 1..11) {
            feetList!!.add(i)
        }

        val feetadapter = ageRCadapter(feetList as List<Int>)
        rpfeet.setAdapter(feetadapter)

        centerPosition = feetList!!.size / 2

        rpfeet.scrollToPosition(centerPosition)

        rpfeet.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val centerposition =
                    layoutManager1.findFirstVisibleItemPosition() + layoutManager1.getChildCount() / 2
                feetadapter.setSelectedItem(centerposition)
                updateCenteredPositionfeet(rpfeet, feetadapter)
            }
        })


        val rpinch = findViewById<RecyclerView?>(R.id.rpinch)

        val layoutManager2 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        rpinch.setLayoutManager(layoutManager2)


        inchlist = ArrayList<Int?>()

        for (i in 1..13) {
            inchlist!!.add(i)
        }

        val inchadapter = ageRCadapter(inchlist as List<Int>)
        rpinch.setAdapter(inchadapter)

        centerPosition = inchlist!!.size / 2

        rpinch.scrollToPosition(centerPosition)

        rpinch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val centerposition =
                    layoutManager2.findFirstVisibleItemPosition() + layoutManager2.getChildCount() / 2
                inchadapter.setSelectedItem(centerposition)
                updateCenteredPositioninch(rpinch, inchadapter)
            }
        })


        val back = findViewById<Button?>(R.id.back)
        back.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                onBackPressed()
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        })

        val selectheighttxt = findViewById<TextView?>(R.id.selectheighttxt)

        val toptobottom = AnimationUtils.loadAnimation(this, R.anim.slidefromtoptobottom)
        toptobottom.setDuration(2000)
        selectheighttxt.startAnimation(toptobottom)

        val lefttoright = AnimationUtils.loadAnimation(this, R.anim.slide_in_left)
        lefttoright.setDuration(2000)
        avatar.startAnimation(lefttoright)

        val righttoleft = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        righttoleft.setDuration(2000)
        llcm.startAnimation(righttoleft)
        llft.startAnimation(righttoleft)

        val llbuttons = findViewById<LinearLayout?>(R.id.llbuttons)
        val bottomtoup = AnimationUtils.loadAnimation(this, R.anim.slidefrombottomtotop)
        bottomtoup.setDuration(2000)
        llbuttons.setAnimation(bottomtoup)


        val nextbtn = findViewById<Button?>(R.id.nextbtn)

        println("kg" + kg)
        println(pounds)
        nextbtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val intent1 = Intent(getApplicationContext(), bmiinformation::class.java)
                intent1.putExtra("selected1", selected1)
                intent1.putExtra("selectedAge", selectedAge.toString())
                if (kg != null) {
                    intent1.putExtra("kg", kg.toString())
                } else {
                    intent1.putExtra("pounds", pounds.toString())
                }
                intent1.putExtra("gender", gender)
                if (cm != null) {
                    intent1.putExtra("cm", cm.toString())
                } else {
                    intent1.putExtra("inch", inch.toString())
                    intent1.putExtra("feet", feet.toString())
                }
                startActivity(intent1)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                f = 1
                finish()
            }
        })
    }

    private fun updateCenteredPositioncm(recyclerView: RecyclerView, adapter: ageRCadapter) {
        val layoutManager = recyclerView.getLayoutManager() as LinearLayoutManager?
        val firstVisiblePosition = layoutManager!!.findFirstVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

        val centerPosition = (firstVisiblePosition + lastVisiblePosition) / 2

        if (centerPosition >= 0 && centerPosition < adapter.getItemCount()) {
            cm = weightList!!.get(centerPosition)
            selectedcm!!.setText(cm.toString())
        }
    }

    private fun updateCenteredPositionfeet(recyclerView: RecyclerView, adapter: ageRCadapter) {
        val layoutManager = recyclerView.getLayoutManager() as LinearLayoutManager?
        val firstVisiblePosition = layoutManager!!.findFirstVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

        val centerPosition = (firstVisiblePosition + lastVisiblePosition) / 2

        if (centerPosition >= 0 && centerPosition < adapter.getItemCount()) {
            feet = feetList!!.get(centerPosition)
            selectedfeet!!.setText(feet.toString())
        }
    }

    private fun updateCenteredPositioninch(recyclerView: RecyclerView, adapter: ageRCadapter) {
        val layoutManager = recyclerView.getLayoutManager() as LinearLayoutManager?
        val firstVisiblePosition = layoutManager!!.findFirstVisibleItemPosition()
        val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()

        val centerPosition = (firstVisiblePosition + lastVisiblePosition) / 2

        if (centerPosition >= 0 && centerPosition < adapter.getItemCount()) {
            inch = inchlist!!.get(centerPosition)
            selectedinch!!.text = inch.toString()
        }
    }

    override fun finish() {
        super.finish()

        if (f == 0) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}