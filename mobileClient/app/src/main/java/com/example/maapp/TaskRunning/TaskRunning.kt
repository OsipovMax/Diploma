package com.example.maapp.TaskRunning

import android.content.Context
import android.os.Build
import android.view.View
import com.example.maapp.CustomTestsLogic.Tests
import com.example.maapp.R
import com.example.maapp.data.entity.DeviceInformation
import com.example.maapp.data.entity.DeviceResponse
import com.example.maapp.data.entity.TestResult
import com.example.maapp.data.network.DeviceTestApi
import com.example.maapp.data.repository.DeviceRepository
import com.example.maapp.presentation.di.NetworkModule
import com.example.maapp.presentation.ui.charts.BandwidthTestChart
import com.example.maapp.presentation.ui.charts.SearchTestChart
import com.example.maapp.presentation.ui.charts.WallTestChart
import com.github.mikephil.charting.charts.BarChart
import com.google.android.material.snackbar.Snackbar
import github.nisrulz.easydeviceinfo.base.EasyCpuMod
import github.nisrulz.easydeviceinfo.base.EasyDeviceMod
import github.nisrulz.easydeviceinfo.base.EasyMemoryMod
import kotlinx.android.synthetic.main.activity_wall_test.*
import kotlinx.android.synthetic.main.activity_wall_test.view.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.random.Random

class TaskRunning {
    // this class provide tasks ruuning
    private var testJob = Job()
    private var tempJob = Job()

    private var sharedValue = 0.0
    var yourDeviceResult: ArrayList<String> = arrayListOf("","")


    fun startTest(view: View, checkFlag: Boolean, borderlineDev: ArrayList<String?>,
                  wallChart: WallTestChart,
                  chartConsumptionGraph: BarChart,
                  pbOut: () -> Unit,
                  chartIn: () -> Unit,
                  enableButton: () ->Unit,
                  resIn: () -> Unit,
                  transerResult: (arg: DeviceResponse, arg2: Int) -> Unit, context: Context) {
        var testResult: Double
        testJob = GlobalScope.launch(Dispatchers.Main) {
            var job = launch {
                chartIn()
                stackChart(wallChart, borderlineDev, chartConsumptionGraph, enableButton)
            }
            var asyncVal = async {
                val testObject = Tests()
                testObject.characteristicFunctionBuildWall(view, checkFlag)
            }
            testResult = asyncVal.await()[0].length
            sharedValue = testResult
            job.join()

            delay(2000)
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            yourDeviceResult[0] = manufacturer + " " + model
            yourDeviceResult[1] = testResult.toString()

            val easyDeviceMod = EasyDeviceMod(context)
            val easyCpuMod = EasyCpuMod()
            val easyMemoryMod = EasyMemoryMod(context)

            val deviceInformation = DeviceInformation(
                brand = manufacturer,
                model = model,
                os = easyDeviceMod.osVersion,
                cpu = easyCpuMod.stringSupported32bitABIS,
                ram = easyMemoryMod.totalRAM.toString(),
                memory = easyMemoryMod.totalInternalMemorySize.toString())

            val deviceResponse = DeviceResponse(manufacturer + " " + model,
                deviceInformation,  testResult)
/*
            if (checkFlag) {
                view.resTextView.text = "Тест завершен. Результат выполнения теста на Вашем устройстве - " +
                        testResult + " м. Это в ${"%.1f".format((borderlineDev[5]!!.toFloat()/testResult))} раз меньше, чем тот результат, " +
                        "который показало 1 в рейтинге устройство - ${borderlineDev[4]}, но в " +
                        "${"%.1f".format((testResult/borderlineDev[7]!!.toFloat()))} больше, чем результат последнего устройства в " +
                        "рейтинге - ${borderlineDev[6]}"
                transerResult(deviceResponse, 1) // with mem
            }
            else {
                view.resTextView.text = "Тест завершен. Результат выполнения теста на Вашем устройстве - " +
                        testResult + " м. Это в ${"%.1f".format((borderlineDev[1]!!.toFloat()/testResult))} раз меньше, чем тот результат, " +
                        "который показало 1 в рейтинге устройство - ${borderlineDev[0]}, но в " +
                        "${"%.1f".format((testResult/borderlineDev[3]!!.toFloat()))} больше, чем результат последнего устройства в " +
                        "рейтинге - ${borderlineDev[2]}"

                transerResult(deviceResponse, 0) // without mem
            }
            */
            pbOut()
            resIn()
        }
    }

    fun stackChart(wallChart: WallTestChart, borderlineDev: ArrayList<String?>
                   , chartConsumptionGraph: BarChart, enableButton: () -> Unit) {
       /* tempJob = GlobalScope.launch(Dispatchers.Main) {
            var yourIncr = 0.0f
            var bestVal = 0.0f
            var yourVal = 0.0f
            var worstVal = 0.0f
            if (borderlineDev.size == 0){
                yourIncr = borderlineDev[2]!!.toFloat() / 1000f
                var yourVal = 0.0f
                for (i in 0 until 1000){
                    delay(5)
                    yourVal += yourIncr
                    wallChart.populateGraphData(0.0f, yourVal, 0.0f, chartConsumptionGraph)
                }
            }
            else {
                bestVal = borderlineDev[1]!!.toFloat()
                worstVal = borderlineDev[3]!!.toFloat()
                wallChart.populateGraphData(bestVal, yourVal, worstVal, chartConsumptionGraph)
                val beginTime = System.currentTimeMillis()
                while (System.currentTimeMillis() - beginTime < 16 * 1000 && sharedValue == 0.0) {
                    delay(230)
                    yourVal = Random.nextInt(30, 500).toFloat()
                    wallChart.populateGraphData(bestVal, yourVal, worstVal, chartConsumptionGraph)
                }
            }
            yourVal = sharedValue.toFloat()
            wallChart.populateGraphData(bestVal, yourVal, worstVal, chartConsumptionGraph)
            sharedValue = 0.0
            enableButton()
            */
        }
    }



    //---------------------------------------------------------------------------------------
/*
    fun startBandwidthTest(view: View, checkFlag: Boolean, borderlineDev: ArrayList<String?>,
                           bandwidthChart: BandwidthTestChart,
                           chart: BarChart, callback: () -> Unit,
                           transerResult: (arg: TestResult) -> Unit){
        var testResult: Int
        testJob = GlobalScope.launch(Dispatchers.Main) {
            val job = launch {
                stackBandwidthChart(view, bandwidthChart, borderlineDev, chart)
            }
            val asyncVal = async {
                val testObject = Tests()
                testObject.bandWidthTest(view,checkFlag)

            }
            job.join()
            println("FINISH")
            println(asyncVal.await())
            testResult = asyncVal.await()

            delay(2000)
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            yourDeviceResult[0] = manufacturer + " " + model
            yourDeviceResult[1] = testResult.toString()

            if (borderlineDev.size == 0){
                bandwidthChart.populateGraphData(
                    0f, testResult.toFloat(),
                    0f, chart
                )
            }
            else {
                bandwidthChart.populateGraphData(
                    borderlineDev[1]!!.toFloat(), testResult.toFloat(),
                    borderlineDev[3]!!.toFloat(), chart
                )
            }
            val bandwidthResult = TestResult(0, manufacturer + " " + model, testResult, null)
            transerResult(bandwidthResult)
            callback()
        }
    }

    fun stackBandwidthChart(view: View, wallChart: BandwidthTestChart, borderlineDev: ArrayList<String?>,
                            chart: BarChart) {
        tempJob = GlobalScope.launch(Dispatchers.Main) {
            var bestIncr = 0f
            var yourIncr = 0f
            var worstIncr = 0f

            if (borderlineDev.size == 0){
                for (i in 0 until 1000) {
                    delay(11)
                    wallChart.populateGraphData(bestIncr, yourIncr, worstIncr, chart)
                    yourIncr += 0.15f
                }
            }
            else {
                val beginTime = System.currentTimeMillis()
                while (System.currentTimeMillis() - beginTime < 15 * 1000) {
                    delay(230)
                    wallChart.populateGraphData(bestIncr, yourIncr, worstIncr, chart)
                    bestIncr = Random.nextInt(50, 70).toFloat()
                    yourIncr = Random.nextInt(10, 30).toFloat()
                    worstIncr = Random.nextInt(0,5).toFloat()
                }
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    fun startSearchTest(view: View, checkFlag: Boolean, searchChart: SearchTestChart,
                        borderlineDev: ArrayList<String?>,
                        chart: BarChart, callback: () -> Unit,
                        transerResult: (arg: TestResult) -> Unit){
        var testResult: ArrayList<Int>
        testJob = GlobalScope.launch(Dispatchers.Main) {
            val job = launch {
                stackSearchChart(view, searchChart, borderlineDev, chart)
            }
            val asyncVal = async {
                val testObject = Tests()
                if (checkFlag) {
                    testObject.searchTest(view)
                }
                else{
                    testObject.quadraticComplexitySearchTest(view)
                }
            }
            job.join()
            println("FINISH")
            println(asyncVal.await())
            testResult = asyncVal.await()

            delay(2000)
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            yourDeviceResult[0] = manufacturer + " " + model
            yourDeviceResult[1] = testResult[1].toString()

            if (testResult[0] == 1){
                val snackbar = Snackbar.make(
                        view!!, "Поиск в пределеах группы удался",
                Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackbar.show()
            }
            else {
                val snackbar = Snackbar.make(
                        view!!, "Поиск в пределеах группы не удался",
                Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackbar.show()
            }

            if (borderlineDev.size == 0){
                searchChart.populateGraphData(
                    0f, testResult[1].toFloat(),
                    0f, chart
                )
            }
            else {
                searchChart.populateGraphData(
                    borderlineDev[1]!!.toFloat(), testResult[1].toFloat(),
                    borderlineDev[3]!!.toFloat(), chart
                )
            }
            val bandwidthResult = TestResult(0, manufacturer + " " + model, testResult[1],null)
            transerResult(bandwidthResult)
            callback()
        }
    }

    fun stackSearchChart(view: View, wallChart: SearchTestChart,
                         borderlineDev: ArrayList<String?>,
                         chart: BarChart) {
        tempJob = GlobalScope.launch(Dispatchers.Main) {
            var bestIncr = 0f
            var yourIncr = 0f
            var worstIncr = 0f

            if (borderlineDev.size == 0){
                for (i in 0 until 1000) {
                    delay(10)
                    wallChart.populateGraphData(bestIncr, yourIncr, worstIncr, chart)
                    yourIncr += 0.15f
                }
            }
            else {
                val beginTime = System.currentTimeMillis()
                while (System.currentTimeMillis() - beginTime < 15 * 1000) {
                    delay(230)
                    wallChart.populateGraphData(bestIncr, yourIncr, worstIncr, chart)
                    bestIncr = Random.nextInt(70, 90).toFloat()
                    yourIncr = Random.nextInt(10, 35).toFloat()
                    worstIncr = Random.nextInt(0,10).toFloat()
                }
            }
        }
    }

}
*/