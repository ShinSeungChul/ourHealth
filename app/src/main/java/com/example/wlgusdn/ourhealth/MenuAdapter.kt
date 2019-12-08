package com.example.wlgusdn.ourhealth

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MenuAdapter(Data : ArrayList<MenuList>) : BaseAdapter() {
    private var menulistdata = ArrayList<MenuList>()
    private var menulistdatacount = 0
    internal var inflater : LayoutInflater? = null

    init {
        menulistdata = Data
        menulistdatacount = Data.size
    }


    override fun getItem(position: Int): Any? {
       return menulistdata[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return menulistdatacount
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            val context = parent.context
            if (inflater == null) {
                inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            }
            convertView = inflater!!.inflate(R.layout.list_menu, parent, false)//change
        }

        //구매글들에 대한 정보들

        val name_txt = convertView!!.findViewById<TextView>(R.id.menuname)
        val recommend_txt = convertView.findViewById<TextView>(R.id.recommend)

        name_txt.setText(menulistdata[position].foodname.toString())
        recommend_txt.setText(menulistdata[position].recommend.toString())


        return convertView!!

    }



}