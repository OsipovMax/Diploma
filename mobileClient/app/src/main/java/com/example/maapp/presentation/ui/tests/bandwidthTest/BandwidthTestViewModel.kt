package com.example.maapp.presentation.ui.tests.bandwidthTest

import android.content.Context
import android.os.Build
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.maapp.CustomTestsLogic.Tests
import com.example.maapp.data.entity.DeviceInformation
import com.example.maapp.data.entity.DeviceResponse
import com.example.maapp.data.entity.RequestTestResult
import com.example.maapp.data.repository.DeviceRepository
import com.example.maapp.presentation.system.onNext
import com.example.maapp.presentation.system.schedulersIoToMain
import com.example.maapp.presentation.ui.base.state.Data
import com.example.maapp.presentation.ui.base.state.Loading
import com.example.maapp.presentation.ui.base.state.Error
import com.example.maapp.presentation.ui.base.state.ViewState
import com.example.maapp.presentation.ui.base.view_model.BaseViewModel
import com.example.maapp.presentation.ui.charts.WallTestChart
import com.example.maapp.presentation.ui.tests.BorderlineDevice
import com.example.maapp.presentation.ui.tests.Modes
import com.github.mikephil.charting.charts.BarChart
import github.nisrulz.easydeviceinfo.base.EasyCpuMod
import github.nisrulz.easydeviceinfo.base.EasyDeviceMod
import github.nisrulz.easydeviceinfo.base.EasyMemoryMod
import kotlinx.android.synthetic.main.activity_bandwith_test.view.*
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class BandwidthTestViewModel @Inject constructor(
    private val repository: DeviceRepository
) : BaseViewModel() {
    val devices : MutableLiveData<ViewState<RequestTestResult>> = MutableLiveData()
    var jobs : ArrayList<Job> = arrayListOf()
    var flag = true
    private var test = Tests()
    private var prv = 0
    private var runningTestJob = Job()
    private var runningDrawChartJob = Job()
    private var showingResultsJob = Job()
    private var runningProgressBarJob = Job()

    init {
        repository.getBorderlineDevicesBandwidth()
            .schedulersIoToMain()
            .toObservable()
            .map<ViewState<RequestTestResult>> {
                Data(it)
            }
            .startWith(Loading)
            .onErrorReturn(::Error)
            .subscribe({
                devices.onNext(it)
            },{
                Timber.d(it)
            }).autoDispose()

    }
    fun sendData(response: DeviceResponse, mode: Modes) {
        if (mode.name == "PROCESSOR") {
            repository.sendBandwidthTestProcResult(response)
                .schedulersIoToMain()
                .subscribe({
                    Timber.d("Успешно")
                }, {
                    Timber.d("Ошибка")
                }).autoDispose()
        }
        else if (mode.name == "MEMORY") {
            repository.sendBandwidthTestMemResult(response)
                .schedulersIoToMain()
                .subscribe({
                    Timber.d("Успешно")
                }, {
                    Timber.d("Ошибка")
                }).autoDispose()
        }
    }

    fun destroyCoroutines() {
        println("destroy coroutines")
        runningTestJob.cancel()
        runningDrawChartJob.cancel()
        showingResultsJob.cancel()
        runningProgressBarJob.cancel()
        for (job in jobs) {
            job.cancel()
        }
        getCoroutineInform()
    }

    suspend fun startProgressBar (view: View, mode: Modes) {
        var progressBarStatus = 0
        if (mode.name == "PROCESSOR"){
            view.BandwidthPbDescrip.text = "Подождите, происходит выполнение теста.\nРежим тестирования: процессор."
        }
        else {
            view.BandwidthPbDescrip.text = "Подождите, происходит выполнение теста.\nРежим тестирования: процессор " +
                    "+ память."
        }
        while (progressBarStatus < 100) {
            progressBarStatus += 1
            try {
                delay(165)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            if(view.BandwidthPrgView != null || view.BandwidthPrgBar != null) {
                view.BandwidthPrgView.text = "$progressBarStatus из 100%"
                view.BandwidthPrgBar.secondaryProgress = progressBarStatus
            }
        }
    }

    fun getDeviceInformation(context: Context
    ): DeviceInformation {
        val easyDeviceMod = EasyDeviceMod(context)
        val easyCpuMod = EasyCpuMod()
        val easyMemoryMod = EasyMemoryMod(context)

        return DeviceInformation(
            brand = Build.MANUFACTURER,
            model = Build.MODEL,
            os = easyDeviceMod.osVersion,
            cpu = easyCpuMod.stringSupported32bitABIS,
            ram = easyMemoryMod.totalRAM.toString(),
            memory = easyMemoryMod.totalInternalMemorySize.toString())
    }

    fun showResult(view: View,
                   mode: Modes,
                   borderlineDev: ArrayList<BorderlineDevice>,
                   testResult: Int
    ) {
        var bestResult = 0
        var worstResult = 0
        var bestDevTitle = ""
        var worstDevTitle = ""
        if (borderlineDev.size == 0){
            view.BandwidthResTextView.text = "Тест завершен. Результат выполнения теста на Вашем устройстве - " +
                    testResult + " автомобилей. "
            return
        }
        if (mode.name == "PROCESSOR"){
            bestResult = borderlineDev[0].result
            bestDevTitle = borderlineDev[0].title.orEmpty()
            worstResult = borderlineDev[1].result
            worstDevTitle = borderlineDev[1].title.orEmpty()
        }
        else {
            bestResult = borderlineDev[2].result
            bestDevTitle = borderlineDev[2].title.orEmpty()
            worstResult = borderlineDev[3].result
            worstDevTitle = borderlineDev[3].title.orEmpty()
        }

        view.BandwidthResTextView.text = "Тест завершен. Результат выполнения теста на Вашем устройстве - " +
                testResult + " автомобилей. "

        var diffRes: Float
        var fmtStr: String
        if (bestResult > testResult) {

            diffRes = bestResult.toFloat() / testResult.toFloat()
            if (diffRes % 1 < 0.1f){
                fmtStr = "%d".format(diffRes.toInt())
            }
            else{
                fmtStr = "%.2f".format(diffRes)
            }
            view.BandwidthResTextView.text = view.BandwidthResTextView.text.toString() + "Это в $fmtStr раз меньше, чем тот результат, " +
                    "который показало 1 в рейтинге устройство - $bestDevTitle. "

        }
        else {
            if (testResult > bestResult) {
                view.BandwidthResTextView.text =
                    view.BandwidthResTextView.text.toString() + "Ваше устройство показало лучший результат. Ваша позиция в рейтенге - 1. "
            }
            else {
                view.BandwidthResTextView.text =
                    view.BandwidthResTextView.text.toString() + "Ваше устройство показало результат аналогичный результату лучшего устройства" +
                            "в рейтинге. Ваша позиция в рейтенге - 1. "
            }
        }

        if (worstResult < testResult) {
            diffRes = testResult.toFloat() / worstResult.toFloat()
            if (diffRes % 1 < 0.1f){
                fmtStr = "%d".format(diffRes.toInt())
            }
            else{
                fmtStr = "%.2f".format(diffRes)
            }
            view.BandwidthResTextView.text = view.BandwidthResTextView.text.toString() + "А также это в $fmtStr раз больше, чем результат " +
                    "последнего устройства в рейтинге - $worstDevTitle. "
        }
        else {
            view.BandwidthResTextView.text = view.BandwidthResTextView.text.toString() + "Ваше устройство показало худший результат среди всех устройств."
        }
    }

    fun runTask(view: View,
                mode: Modes,
                borderlineDev: ArrayList<BorderlineDevice>,
                chart: WallTestChart,
                chart_view: BarChart,
                context: Context,
                chartIn: () -> Unit,
                enableButton: () -> Unit,
                progressBarOut: () -> Unit,
                resultBatIn: () -> Unit) {

        destroyCoroutines()
        var testResult = 0

        runningDrawChartJob = GlobalScope.launch(Dispatchers.Main, start = CoroutineStart.LAZY) {
            view.runBandwidthTask.isEnabled = false
            chartIn()
            chart.stackChart(mode, borderlineDev, chart_view, enableButton, "bandwidth")
            if (borderlineDev.size >= 4) {
                if (mode.name == "PROCESSOR"){
                    chart.populateGraphData(borderlineDev[0].result, testResult, borderlineDev[1].result, chart_view, "bandwidth")
                }
                else {
                    chart.populateGraphData(borderlineDev[2].result, testResult, borderlineDev[3].result, chart_view, "bandwidth")
                }
            }
            else {
                chart.populateGraphData(0, testResult, 0, chart_view, "bandwidth")
            }
        }

        runningProgressBarJob = GlobalScope.launch(Dispatchers.Main, start = CoroutineStart.LAZY) {
            startProgressBar(view, mode)
            showingResultsJob.start()
        }

        showingResultsJob = GlobalScope.launch(Dispatchers.Main, start = CoroutineStart.LAZY) {
            showResult(view, mode, borderlineDev, testResult)
            progressBarOut()
            resultBatIn()
            delay(2500)
            view.runBandwidthTask.isEnabled = true
        }

        runningTestJob = test.computingScope.launch(start = CoroutineStart.LAZY) {
            jobs = test.bandwidthCoroutineFunc(mode)

            for (job in jobs) {
                job.start()
            }
            runningDrawChartJob.start()
            runningProgressBarJob.start()

            for (job in jobs) {
                job.join()
            }

            val deviceInformation = getDeviceInformation(context)
            if (flag) {
                testResult = test.getRes()
                prv = testResult
                flag = false
            }
            else {
                testResult = prv
            }

            if (mode.name == "PROCESSOR") {
                sendData(DeviceResponse((deviceInformation.brand + " " + deviceInformation.model), deviceInformation, testResult.toDouble()), Modes.PROCESSOR)
            }
            else {
                sendData(DeviceResponse((deviceInformation.brand + " " + deviceInformation.model), deviceInformation, testResult.toDouble()), Modes.MEMORY)
            }
        }
        runningTestJob.start()
    }

    fun getCoroutineInform(){
        println("test job - $runningTestJob")
        println("draw chart job - $runningDrawChartJob")
        println("show result job - $showingResultsJob")
        println("progressBar job - $runningProgressBarJob")
        for (job in jobs) {
            println("computing jobs - $job")
        }
    }
}