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


    var xAxes : ArrayList<String> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.trends, container, false) as View
        listview = view.findViewById(R.id.list)

        trendata.add(TrendList("지방섭취가 평균보다 높습니다"))
        trendata.add(TrendList("최근 칼로리 섭취가 높습니다"))
        trendata.add(TrendList("식사패턴이 불규칙 합니다."))


        val adapter = TrendAdapter(trendata)
        adapter.notifyDataSetChanged()
        listview.adapter = adapter

        fun generateLineData() : LineData
        {
            var data = LineData()
            var entry = ArrayList<Entry>()

            for(x in 1 until 13)
            {
                val randomValues =  Random.nextInt(1500, 1800)
                entry.add(Entry(x.toFloat(),randomValues.toFloat()))
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

            for(x in 1 until 13)
            {
                val randomValues =  Random.nextInt(2000, 3000)
                entry.add(BarEntry(x.toFloat(),randomValues.toFloat()))
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
        xAxis.setAxisMaxValue(12f)
        xAxis.setLabelCount(12,true)

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
        chart.invalidate()

// Create an ArrayAdapter using the string array and a default spinner layout





        return view
    }


    /*inner class CustomBar(chart: BarDataProvider?, animator: ChartAnimator?, viewPortHandler: ViewPortHandler?) : BarChartRenderer(chart, animator, viewPortHandler)
    {
        override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
            val trans = mChart.getTransformer(dataSet.axisDependency)

            val phaseX = mAnimator.phaseX
            val phaseY = mAnimator.phaseY

             var buffer = mBarBuffers[index]
            buffer.setPhases(phaseX, phaseY)
            buffer.setDataSet(index)
            buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
            //....
            c.drawRoundRect(
                    buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], cornersDimen, cornersDimen, mRenderPaint
            )
            //....
        }
    }*/


}
