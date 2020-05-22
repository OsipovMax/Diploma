package com.example.maapp.RootFragments

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.solver.widgets.Analyzer.setPosition
import android.util.AttributeSet
import android.view.View
import com.example.maapp.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class TestActiv : AppCompatActivity() {

    lateinit var barChartView : HorizontalBarChart

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        populateGraphData(7,5,3)
    }



    fun populateGraphData(a : Int, b: Int, c: Int) {
        barChartView = findViewById<HorizontalBarChart>(R.id.chartConsumptionGraph)

        val barWidth = 2f
        val yValueGroup1 = ArrayList<BarEntry>()
        val barDataSet1: BarDataSet
        val barData: BarData
        val legend = barChartView.legend
        val legendEntries = arrayListOf<LegendEntry>()
        val xAxis = barChartView.xAxis
        val leftAxis = barChartView.axisLeft

        yValueGroup1.add(BarEntry(2f, a.toFloat()))
        yValueGroup1.add(BarEntry(6f, b.toFloat()))
        yValueGroup1.add(BarEntry(10f, c.toFloat()))

        barDataSet1 = BarDataSet(yValueGroup1, "")
        barDataSet1.setColors(Color.BLUE, Color.RED, Color.GREEN)
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(false)

        barData = BarData(barDataSet1)
        barData.setValueFormatter(LargeValueFormatter())

        barChartView.description.isEnabled = false
        barChartView.description.textSize = 0f
        barChartView.xAxis.axisMinimum = 0f
        barChartView.xAxis.axisMaximum = 100f
        barChartView.setVisibleXRangeMaximum(12f)
        barChartView.setVisibleXRangeMinimum(12f)
        barChartView.axisRight.isEnabled = false
        barChartView.setScaleEnabled(false)
        barChartView.invalidate()

        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.yOffset = 2f
        legend.xOffset = 2f
        legend.yEntrySpace = 1f
        legend.textSize = 5f

        //legendEntries.add(LegendEntry("Best", Legend.LegendForm.SQUARE, 10f, 8f, null, Color.BLUE))
        //legendEntries.add(LegendEntry("Your", Legend.LegendForm.SQUARE, 10f, 8f, null, Color.RED))
        //legendEntries.add(LegendEntry("Worst", Legend.LegendForm.SQUARE, 10f, 8f, null, Color.GREEN))
        //legend.setCustom(legendEntries)

        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 9f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter()
        xAxis.labelCount = 12
        xAxis.mAxisMaximum = 12f
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 4f
        xAxis.spaceMax = 4f

        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(false)
        leftAxis.setDrawAxisLine(true)
        leftAxis.spaceTop = 3f
        leftAxis.axisMinimum = 0f
        leftAxis.axisMaximum = 50f

        barChartView.axisLeft.setDrawLabels(false)

        barChartView.data = barData
        barChartView.data.barWidth = barWidth
        barChartView.setDrawValueAboveBar(true)
        barChartView.setVisibleXRange(1f, 12f)
    }

    fun temp (view: View) {
        GlobalScope.launch(Dispatchers.Main) {
            for (i in 0 until 1000){
                delay(100)
                val a = Random.nextInt(1, 7)
                val b = Random.nextInt(1, 9)
                val c = Random.nextInt(1, 8)
                populateGraphData(a,b,c)
            }
        }
    }
}
