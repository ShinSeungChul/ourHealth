package com.example.wlgusdn.ourhealth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import com.example.wlgusdn.ourhealth.FoodPopup.Companion.llist
import type.FoodDataInput

class FoodListActivity : AppCompatActivity
{
    var list : ListView?=null
    var data : ArrayList<FoodDataInput> = ArrayList<FoodDataInput>()

    constructor(data : ArrayList<FoodDataInput>)
    {
        this.data=data
    }
    constructor()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_selectfood)

        list = findViewById(R.id.select_list)
        Log.d("asdasd",llist!!.size.toString())


        var adap = FoodListAdapter(llist!!)
        adap.notifyDataSetChanged()
        Log.d("asdasd","11")
        list!!.adapter=adap
        adap.notifyDataSetChanged()
        Log.d("asdasd","22")
        setListViewHeightBasedOnChildren(list!!)


    }
    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter
            ?: // pre-condition
            return

        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)

        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
    }
}