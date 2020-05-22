package com.example.maapp.presentation.ui.charts

import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.LargeValueFormatter

class SearchTestChart {
    lateinit var barChartView : BarChart

    fun populateGraphData(a : Float, b: Float, c: Float, chartConsumptionGraph: BarChart) {
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
        barDataSet1.setColors(Color.GRAY, Color.DKGRAY, Color.BLACK)
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(false) ///!!!

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

        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)
        legend.yOffset = 2f
        legend.xOffset = 2f
        legend.yEntrySpace = 1f
        legend.textSize = 5f

        legendEntries.add(LegendEntry("Best", Legend.LegendForm.SQUARE, 10f, 8f, null, Color.GRAY))
        legendEntries.add(LegendEntry("Your", Legend.LegendForm.SQUARE, 10f, 8f, null, Color.DKGRAY))
        legendEntries.add(LegendEntry("Worst", Legend.LegendForm.SQUARE, 10f, 8f, null, Color.BLACK))
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
        //leftAxis.axisMaximum = 500f ///11111111111111

        barChartView.legend.isEnabled = true
        //barChartView.setDrawValueAboveBar(true)
        barChartView.axisRight.axisMinimum = 50000f


        barChartView.axisLeft.setDrawLabels(false)
        barChartView.xAxis.setDrawLabels(false)

        barChartView.data = barData
        barChartView.data.barWidth = barWidth

        barChartView.setVisibleXRange(1f, 12f)
    }
}