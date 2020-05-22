package com.example.maapp.presentation.ui.charts

import android.graphics.Color
import android.view.View
import com.example.maapp.presentation.ui.tests.BorderlineDevice
import com.example.maapp.presentation.ui.tests.Modes
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import kotlinx.android.synthetic.main.activity_wall_test.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class WallTestChart () {

    lateinit var barChartView : BarChart

    fun populateGraphData(a: Int, b: Int, c: Int, chartConsumptionGraph: BarChart, testTitle: String) {
        barChartView = chartConsumptionGraph

        val barWidth = 2f
        val yValueGroup1 = ArrayList<BarEntry>()
        val barDataSet1: BarDataSet
        val barData: BarData
        val legend = barChartView.legend
        val legendEntries = arrayListOf<LegendEntry>()
        val xAxis = barChartView.xAxis
        val leftAxis = barChartView.axisLeft

        var xAxisValues = ArrayList<String>()
        xAxisValues.add("Худший")
        xAxisValues.add("Ваш")
        xAxisValues.add("Лучший")


        yValueGroup1.add(BarEntry(2f, a.toFloat()))
        yValueGroup1.add(BarEntry(6f, b.toFloat()))
        yValueGroup1.add(BarEntry(10f, c.toFloat()))

        barDataSet1 = BarDataSet(yValueGroup1, "")

        var bestColor = Color.rgb(0,0,0)
        var yourColor = Color.rgb(0,0,0)
        var worstColor = Color.rgb(0,0,0)
        if (testTitle == "wall"){
            bestColor = Color.rgb(0, 0, 250)
            yourColor = Color.rgb(100,150,50)
            worstColor = Color.rgb(100, 0, 50)
        }
        else if (testTitle == "search"){
            bestColor = Color.rgb(210, 210, 120)
            yourColor = Color.rgb(230,55,140)
            worstColor = Color.rgb(100, 180, 180)
        }
        else if (testTitle == "bandwidth"){
            bestColor = Color.rgb(180, 80, 60)
            yourColor = Color.rgb(200,230,140)
            worstColor = Color.rgb(170, 140, 200)
        }

        barDataSet1.setColors(bestColor, yourColor, worstColor)

        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(true)

        barData = BarData(barDataSet1)
        barData.setValueFormatter(LargeValueFormatter())

        barChartView.description.isEnabled = false
        barChartView.description.textSize = 0f
        barChartView.xAxis.axisMinimum = 0f
        barChartView.xAxis.axisMaximum = 15f
        barChartView.setVisibleXRangeMaximum(12f)
        barChartView.setVisibleXRangeMinimum(12f)
        barChartView.axisRight.isEnabled = true
        barChartView.setScaleEnabled(false)


        barChartView.invalidate()

        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(true)
        legend.yOffset = 2f
        legend.xOffset = 2f
        legend.yEntrySpace = 1f
        legend.textSize = 10f



        legendEntries.add(
            LegendEntry(
                "Лучший результат",
                Legend.LegendForm.SQUARE,
                10f,
                8f,
                null,
                bestColor
            )
        )

        legendEntries.add(
            LegendEntry(
                "Ваш Результат",
                Legend.LegendForm.SQUARE,
                10f,
                8f,
                null,
                yourColor
            )
        )

        legendEntries.add(
            LegendEntry(
                "Худший результат",
                Legend.LegendForm.SQUARE,
                10f,
                8f,
                null,
                worstColor
            )
        )


        legend.setCustom(legendEntries)

        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 7f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)
        xAxis.labelCount = 12
        xAxis.mAxisMaximum = 12f
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 4f
        xAxis.spaceMax = 4f



        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(false)
        //leftAxis.setDrawAxisLine(true)
        leftAxis.spaceTop = 3f
        leftAxis.axisMinimum = 0f
        //leftAxis.axisMaximum = 500f ///

        barChartView.legend.isEnabled = true
        barChartView.axisRight.axisMinimum = 50000f


        barChartView.axisLeft.setDrawLabels(false)
        barChartView.axisRight.setDrawLabels(false)
        barChartView.xAxis.setDrawLabels(false)

        barChartView.axisLeft.setDrawGridLines(false)
        barChartView.axisRight.setDrawGridLines(false)
        barChartView.xAxis.setDrawGridLines(false)

        barChartView.axisLeft.setDrawAxisLine(false)
        barChartView.axisRight.setDrawAxisLine(false)
        barChartView.xAxis.setDrawAxisLine(false)

        barChartView.data = barData
        barChartView.data.barWidth = barWidth

        barChartView.setVisibleXRange(1f, 12f)


    }

    suspend fun stackChart(mode: Modes,
                           borderlineDev: ArrayList<BorderlineDevice>,
                           chartConsumptionGraph: BarChart,
                           enableButton: () -> Unit,
                           testTitle: String) {
            var yourIncr = 0
            var bestVal = 0
            var yourVal = 0
            var worstVal = 0
            if (borderlineDev.size == 0){
                /*yourIncr = borderlineDev[2] / 1000f
                var yourVal = 0.0f
                for (i in 0 until 1000){
                    delay(5)
                    yourVal += yourIncr
                    populateGraphData(0.0f, yourVal, 0.0f, chartConsumptionGraph)
                }*/
            }
            else {
                if (mode.name == "PROCESSOR"){
                    bestVal = borderlineDev[0].result
                    worstVal = borderlineDev[1].result
                }
                else{
                    bestVal = borderlineDev[2].result
                    worstVal = borderlineDev[3].result
                }

                populateGraphData(bestVal, yourVal, worstVal, chartConsumptionGraph, testTitle)
                val beginTime = System.currentTimeMillis()
                while (System.currentTimeMillis() - beginTime < 16 * 1000) {
                    delay(230)
                    yourVal = Random.nextInt(30 , bestVal - 1)
                    populateGraphData(bestVal, yourVal, worstVal, chartConsumptionGraph, testTitle)
                }
            }
    }

}