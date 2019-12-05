package com.example.wlgusdn.ourhealth

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlin.random.Random

class TodayFragment : Fragment()
{
    lateinit var text : TextView

    private lateinit var listview : ListView
    var todaydata : ArrayList<TodayKcalList> = ArrayList()



    val singleton : HealthData_Singleton = HealthData_Singleton.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.today, container, false) as View
        text = view.findViewById(R.id.editHealthDateValue1)
        text.setText("1280(-" + singleton.kcal+")")

        val chart = view.findViewById<BarChart>(R.id.chart)
        listview = view.findViewById(R.id.list)


        //todaydata = singleton.GetTodayKcalList()
        todaydata.add(TodayKcalList("09:00","짜장면",863,0,0,0))
        todaydata.add(TodayKcalList("13:00","짜장면",863,0,0,0))
        todaydata.add(TodayKcalList("18:00","짜장면",863,0,0,0))
        todaydata.add(TodayKcalList("19:00","짜장면",863,0,0,0))
        todaydata.add(TodayKcalList("21:00","짜장면",863,0,0,0))
        todaydata.add(TodayKcalList("29:00","짜장면",863,0,0,0))


        val adapter = TodayKcalAdapter(todaydata)
        adapter.notifyDataSetChanged()
        listview.adapter = adapter


        val entries = ArrayList<BarEntry>()



        for(x in 0..24)
        {
            val randomValues =  Random.nextInt(0, 800)
            if(x== 9 || x== 13 || x==18 || x== 23)
                entries.add(BarEntry(x.toFloat(), randomValues.toFloat()))


        }

        val set = BarDataSet(entries, "BarDataSet")
        set.setColor(Color.parseColor("#FF8EFF7F"))
        val data = BarData(set)
        data.barWidth = 0.6f // set custom bar width
        data.setValueTextSize(10f)

        chart.data = data
        //chart.setBackgroundColor(Color.LTGRAY)
        val xAxis = chart.xAxis
        val leftAxis = chart.axisLeft
        val rightAxis = chart.axisRight
        xAxis.axisLineWidth = 3f
        xAxis.textSize = 10f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setAxisMinValue(0f)
        xAxis.setAxisMaxValue(24f)
        xAxis.setLabelCount(5,true)
        leftAxis.setAxisMinValue(0f)
        leftAxis.setAxisMaxValue(2400f)
        leftAxis.setDrawAxisLine(false)
        leftAxis.setLabelCount(5,true)
        //leftAxis.isEnabled = false
        leftAxis.setDrawGridLines(false)
        rightAxis.isEnabled = false
        xAxis.setDrawGridLines(false)
        chart.setFitBars(true) // make the x-axis fit exactly all bars
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.invalidate() // refresh

        return view
    }
}