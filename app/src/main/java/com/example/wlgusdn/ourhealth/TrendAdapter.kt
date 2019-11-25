package com.example.wlgusdn.ourhealth
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.wlgusdn.ourhealth.TrendList

class TrendAdapter(Data : ArrayList<TrendList>) : BaseAdapter()
{
    private var trendList = ArrayList<TrendList>()
    private var trendList_count = 0
    internal var inflater: LayoutInflater? = null

    init {

        trendList = Data
        trendList_count = Data.size

    }


    override fun getCount(): Int {

        return trendList_count
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
            convertView = inflater!!.inflate(R.layout.trend_listitem, parent, false)//change
        }

        //구매글들에 대한 정보들



        val analyztext = convertView!!.findViewById<TextView>(R.id.analyze)


        analyztext.setText(trendList[position].analyz)


        return convertView!!
    }
}