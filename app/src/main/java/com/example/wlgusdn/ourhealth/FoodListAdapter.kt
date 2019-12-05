package com.example.wlgusdn.ourhealth

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import type.FoodDataInput
import java.util.*


class FoodListAdapter(Data: ArrayList<FoodDataInput>) : BaseAdapter() {
    private var chatRoomListData = ArrayList<FoodDataInput>()
    private var chatRoomListDataCount = 0
    internal var inflater: LayoutInflater? = null

    init {

        chatRoomListData = Data
        chatRoomListDataCount = Data.size

    }


    override fun getCount(): Int {

        return chatRoomListDataCount
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val context = parent.context
            if (inflater == null) {
                inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            }
            convertView = inflater!!.inflate(R.layout.food_list, parent, false)//change
        }

        //구매글들에 대한 정보들

        val Name = convertView!!.findViewById<TextView>(R.id.list_name)
        val Kcal = convertView.findViewById<TextView>(R.id.list_kcal)
        val Pro = convertView.findViewById<TextView>(R.id.list_pro)
        val Car = convertView.findViewById<TextView>(R.id.list_car)//change
        val Fat = convertView.findViewById<TextView>(R.id.list_fat)//change
       Name.setText(chatRoomListData[position].id().toString())
        Kcal.setText(chatRoomListData[position].cal().toString())
        Pro.setText(chatRoomListData[position].pro().toString())
        Car.setText(chatRoomListData[position].car().toString())//change
        Fat.setText(chatRoomListData[position].fat().toString())

        return convertView!!
    }



}