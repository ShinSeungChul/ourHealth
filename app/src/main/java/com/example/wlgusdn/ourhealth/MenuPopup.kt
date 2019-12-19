package com.example.wlgusdn.ourhealth

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.TextView

class MenuPopup : Activity() {

    lateinit var menuname : String
    lateinit var kcal : String
    lateinit var car : String
    lateinit var pro : String
    lateinit var fat : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_menu_popup)

        val menunametxt = findViewById<TextView>(R.id.menuname)
        val kcaltxt = findViewById<TextView>(R.id.kcal)
        val cartxt = findViewById<TextView>(R.id.car)
        val protxt = findViewById<TextView>(R.id.pro)
        val fattxt = findViewById<TextView>(R.id.fat)

        val intent = intent
        menuname = intent.extras.getString("menuname")
        kcal = intent.extras.getString("kcal")
        car = intent.extras.getString("car")
        pro = intent.extras.getString("pro")
        fat = intent.extras.getString("fat")

        menunametxt.setText(menuname)
        kcaltxt.setText(kcal)
        cartxt.setText(car)
        protxt.setText(pro)
        fattxt.setText(fat)





    }
}
