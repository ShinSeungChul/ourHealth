package com.example.wlgusdn.ourhealth
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class TodayKcalAdapter(Data : ArrayList<TodayKcalList>) : BaseAdapter()
{
    private var todayKcalList = ArrayList<TodayKcalList>()
    private var todayKcalList_count = 0
    internal var inflater: LayoutInflater? = null

    init {

        todayKcalList = Data
        todayKcalList_count = Data.size

    }


    override fun getCount(): Int {

        return todayKcalList_count
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
            convertView = inflater!!.inflate(R.layout.todaykcal_listitem, parent, false)//change
        }

        //구매글들에 대한 정보들



        val time = convertView!!.findViewById<TextView>(R.id.time)
        val food = convertView!!.findViewById<TextView>(R.id.foodname)
        val kcal = convertView!!.findViewById<TextView>(R.id.kcal)

        time.setText(todayKcalList[position].time +":00")
        food.setText(todayKcalList[position].foodname)
        kcal.setText(todayKcalList[position].kcal.toString() + " kcal")

        return convertView!!
    }
}