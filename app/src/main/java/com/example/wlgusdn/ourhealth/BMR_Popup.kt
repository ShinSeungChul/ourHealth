package com.example.wlgusdn.ourhealth

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.Window.FEATURE_NO_TITLE
import android.content.Intent
import android.widget.*


class BMR_Popup : Activity() {

    lateinit var result : TextView
    lateinit var title : TextView
    lateinit var att : TextView
    lateinit var edit : EditText
    lateinit var unit : TextView
    lateinit var btn : Button
    var height : Float = 0f
    var weight : Float = 0f
    var age : Int = 0
    var bmr : Float = 0f
    var sex : String = "man"
    var active : Int = 0
    var recommended_kcal : Double = 0.0

    lateinit var editor : LinearLayout
    lateinit var radiosex : RadioGroup
    lateinit var radioactive : RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.bmr_popup)



        var count = 0
        result = findViewById(R.id.result)
        editor = findViewById(R.id.editor)
        radiosex = findViewById(R.id.radiosex)
        radioactive = findViewById(R.id.radioactive)
        title = findViewById(R.id.title)
        att  = findViewById(R.id.attribute)
        edit = findViewById(R.id.edit)
        unit = findViewById(R.id.unit)
        btn = findViewById(R.id.next)
        val titletxt = arrayOf("키를 입력하세요","몸무게를 입력하세요","나이를 입력하세요","활동량을 선택해주세요","권장칼로리 측정결과")
        val atttxt = arrayOf("키 ","몸무게 ","나이 ","활동량 ","권장칼로리 ")
        val unittxt = arrayOf(" cm"," kg"," 세",""," kcal")


        btn.setOnClickListener {
            when(count)
            {
                0 -> {
                    //radio sex click
                    radiosex.visibility = View.GONE
                    editor.visibility = View.VISIBLE
                    edit.setText("")
                }
                1 -> {
                    height = edit.text.toString().toFloat()
                    edit.setText("")

                }
                2 -> {
                    weight = edit.text.toString().toFloat()
                    edit.setText("")


                }
                3 -> {
                    age = edit.text.toString().toInt()
                    edit.setText("")
                    editor.visibility = View.GONE
                    radioactive.visibility = View.VISIBLE


                }
                4->
                {
                    radioactive.visibility = View.GONE
                    editor.visibility = View.VISIBLE
                    result.visibility = View.VISIBLE
                    edit.visibility = View.GONE
                    result.setText(Compute_Recommended_Kcal().toString())
                }

            }
            if(count < 5)//ui change twice
            {
                title.text = titletxt[count]
                att.text = atttxt[count]
                unit.text = unittxt[count]
                count++//finally count is 2
            }


        }












    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event!!.action == MotionEvent.ACTION_OUTSIDE){return false}
        return true
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_m ->
                    if (checked) {
                        sex = "man"

                    }
                R.id.radio_w ->
                    if (checked) {
                        sex = "woman"
                    }
                R.id.radio_1 ->
                    if (checked) {
                        active = 1
                    }
                R.id.radio_2 ->
                    if (checked) {
                        active = 2
                    }
                R.id.radio_3 ->
                    if (checked) {
                        active = 3
                    }
            }
        }
    }


    fun Compute_Recommended_Kcal() : Int
    {
        if(sex == "man")
        {
            bmr = (66.47 +(13.75*weight) + (5 * height) - (6.76 * age)).toFloat()
            Toast.makeText(this,height.toString()+"  "+age.toString()+"  "+weight.toString()+"  "+active.toString(),Toast.LENGTH_LONG).show()
            when(active)
            {
                1->{recommended_kcal = bmr*1.4 }
                2->{recommended_kcal = bmr*1.7}
                3->{recommended_kcal = bmr*1.9}
            }

        }
        else if(sex == "woman")
        {
            bmr = (655.1 +(9.56*weight) + (1.85 * height) - (4.68 * age)).toFloat()
            when(active)
            {
                1->{recommended_kcal = bmr*1.4 }
                2->{recommended_kcal = bmr*1.6}
                3->{recommended_kcal = bmr*1.8}
            }
        }

        return recommended_kcal.toInt()
    }

}