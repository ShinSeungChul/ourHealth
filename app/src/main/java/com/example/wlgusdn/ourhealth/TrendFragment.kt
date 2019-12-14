package com.example.wlgusdn.ourhealth

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import android.R.attr.entries
import android.R.attr.entries
import android.graphics.Canvas
import android.graphics.Color
import android.os.Debug
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.buffer.BarBuffer
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class TrendFragment : Fragment()
{


    private lateinit var listview : ListView
    lateinit var chart : CombinedChart
    var trendata : ArrayList<TrendList> = ArrayList()
    val singleton : HealthData_Singleton = HealthData_Singleton.getInstance()
    lateinit var day30List : Array<Day30List>

    var xAxes : ArrayList<String> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.trends, container, false) as View

        day30List = singleton.day30Lists//0-29 29가 최근(오늘)

        listview = view.findViewById(R.id.list)

        var day5List : ArrayList<Day30List> = ArrayList()
        for(i in 24 until 29)
        {
            if(day30List[i].kcal != null)
                day5List.add(day30List[i])
        }

        var recentfat = 0f
        var recentpro = 0f
        var recentcar = 0f
        var recentcal = 0f
        for(i in 0 until day5List.size)
        {
            recentcal += day5List[i].kcal!!
            recentcar += day5List[i].carbohydrate!!
            recentfat += day5List[i].fat!!
            recentpro += day5List[i].protein!!
        }
        recentcal = recentcal/5
        recentcar = recentcar/5
        recentpro = recentpro/5
        recentfat = recentfat/5

        if(recentcal > singleton.clientData.calavg!!)
        {
            trendata.add(TrendList("최근 칼로리 섭취가 높습니다"))
        }
        if(recentcar > singleton.clientData.tcar!!)
        {
            trendata.add(TrendList("최근 탄수화물 섭취가 높습니다"))
        }
        if(recentpro < singleton.clientData.tpro!!)
        {
            trendata.add(TrendList("최근 단백질 섭취가 낮습니다"))
        }
        if(recentfat > singleton.clientData.tfat!!)
        {
            trendata.add(TrendList("최근 지방 섭취가 높습니다"))
        }
        if(singleton.clientData.carper!! > 50)
        {
            trendata.add(TrendList("탄수화물 비율이 너무 높습니다"))
        }
        if(singleton.clientData.proper!! < 30)
        {
            trendata.add(TrendList("단백질 섭취가 부족합니다"))
        }
        if(singleton.clientData.fatper!! > 20)
        {
            trendata.add(TrendList("지방의 비율이 너무 높습니다"))
        }






        val adapter = TrendAdapter(trendata)
        adapter.notifyDataSetChanged()
        listview.adapter = adapter

        fun generateLineData() : LineData
        {
            var data = LineData()
            var entry = ArrayList<Entry>()

            for(x in 1 until 31)
            {

                entry.add(Entry(x.toFloat(),day30List[x-1].getWorkoutKcal().toFloat()))

            }
            var set : LineDataSet = LineDataSet(entry,"Fat")
            set.setColor(Color.parseColor("#000000"))
            set.axisDependency = YAxis.AxisDependency.LEFT
            data.addDataSet(set)
            return data
        }

        fun generateBarData() : BarData
        {
            var data = BarData()
            var entry = ArrayList<BarEntry>()

            for(x in 1 until 31)
            {
                val randomValues =  Random.nextInt(2000, 3000)
                //entry.add(BarEntry(x.toFloat(),randomValues.toFloat()))
                if(day30List[x-1].kcal != null)
                {
                    entry.add(BarEntry(x.toFloat(),day30List[x-1].kcal!!.toFloat()))
                }
                else
                {
                    entry.add(BarEntry(x.toFloat(),0f))
                }

            }
            var set : BarDataSet = BarDataSet(entry,"Kcal")
            set.setColor(Color.parseColor("#FF8EFF7F"))
            set.axisDependency = YAxis.AxisDependency.LEFT
            data.addDataSet(set)
            data.setValueTextSize(10f)
            data.barWidth = 0.5f // set custom bar width


            return data
        }

        chart = view.findViewById(R.id.chart)
        chart.drawOrder = arrayOf(CombinedChart.DrawOrder.BAR,CombinedChart.DrawOrder.LINE)

        val xAxis = chart.xAxis
        val leftAxis = chart.axisLeft
        val rightAxis = chart.axisRight

        var datas : CombinedData = CombinedData()
        datas.setData(generateBarData())
        datas.setData(generateLineData())
        xAxis.axisLineWidth = 3f
        xAxis.textSize = 10f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setAxisMinValue(1f)
        xAxis.setAxisMaxValue(30f)
        xAxis.setLabelCount(30,true)

        //xAxis.granularity = 1f
        leftAxis.setAxisMinValue(0f)
        leftAxis.setAxisMaxValue(3000f)
        leftAxis.setDrawAxisLine(false)
        leftAxis.setLabelCount(5,true)
        //leftAxis.isEnabled = false
        leftAxis.setDrawGridLines(false)
        leftAxis.isEnabled = false
        rightAxis.isEnabled = false
        xAxis.setDrawGridLines(false)

        chart.description.isEnabled = false

        //chart.legend.isEnabled = false



        chart.data = datas
        chart.data.isHighlightEnabled = false
        chart.invalidate()

// Create an ArrayAdapter using the string array and a default spinner layout





        return view
    }

}


