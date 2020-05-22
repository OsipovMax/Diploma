package com.example.maapp.presentation.ui.tests.wallTest

import android.content.Context
import android.os.Bundle
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import com.example.maapp.CustomTestsFragments.DescriptionTestDialog
import com.example.maapp.CustomTestsFragments.ResultsTestDialog
import com.example.maapp.CustomTestsLogic.Tests
import com.example.maapp.R
import com.example.maapp.RootFragments.MainActivity
import com.example.maapp.TaskRunning.TaskRunning
import com.example.maapp.data.entity.RequestTestResult
import com.example.maapp.presentation.system.getViewModel
import com.example.maapp.presentation.system.observe
import com.example.maapp.presentation.ui.base.BaseFragment
import com.example.maapp.presentation.ui.base.state.Data
import com.example.maapp.presentation.ui.base.state.Loading
import com.example.maapp.presentation.ui.base.state.ViewState
import com.example.maapp.presentation.ui.base.view_model.showSnackMessage
import com.example.maapp.presentation.ui.charts.WallTestChart
import com.example.maapp.presentation.ui.tests.BorderlineDevice
import com.example.maapp.presentation.ui.tests.Modes
import com.github.mikephil.charting.charts.BarChart
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_bandwith_test.*
import kotlinx.android.synthetic.main.activity_wall_test.*
import kotlinx.android.synthetic.main.wall_bottom_sheet.*
import kotlinx.coroutines.*
import java.lang.Error
import javax.inject.Inject
import javax.inject.Provider

class WallTestFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.activity_wall_test
    @Inject
    lateinit var viewModelProvider: Provider<WallTestViewModel>
    private val viewModel by lazy { getViewModel(viewModelProvider) }
    private var borderlineDeviceStorage : ArrayList<BorderlineDevice> = arrayListOf()
    lateinit var  runTask : TaskRunning
    private var mode = Modes.PROCESSOR
    private var firstRunning = true
    var wallChart = WallTestChart()


    override fun onAttach(context: Context) {
        appComponent.inject2(this@WallTestFragment)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(viewModel.devices, ::updateBestWorstDeivces)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tests_menu, menu)
        menu.findItem(R.id.proc).isChecked = true
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.proc -> {
                item.isChecked = true
                val snackbar = Snackbar.make(
                    view!!, "Вы выбрали режим тестирования процессора",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackbar.show()
                mode = Modes.PROCESSOR
            }
            R.id.mem -> {
                item.isChecked = true
                val snackbar = Snackbar.make(
                    view!!, "Вы выбрали режим тестирования процессора и памяти",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackbar.show()
                mode = Modes.MEMORY
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar(wall_test_tb)
        wall_test_tb.title = "Тест - \"Китайская стена\""
        runTask = TaskRunning()

        aboutWallTest.setOnClickListener{
            val sheetTag = "someTag"
            val temp : ArrayList<String?> = ArrayList(1)
            temp.add("1")
            val descriptionBottomSheet = DescriptionTestDialog.create(temp)
            descriptionBottomSheet.show(activity!!.supportFragmentManager, sheetTag)
        }
        runWallTask.setOnClickListener {
            beginTest(view, mode, wallChart, WallChartConsumptionGraph)
        }
        wallChart.populateGraphData(0, 0, 0, WallChartConsumptionGraph, "wall")
        //testDescriptionTestView.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        descriptionCardIn()
    }

    private fun updateBestWorstDeivces (state: ViewState<RequestTestResult>) {
        when (state) {
            is Data -> {
                val bestProcMode = BorderlineDevice(state.data.bestDevice.device, state.data.bestDevice.result!!.toInt())
                val worstProcMode = BorderlineDevice(state.data.worstDevice.device, state.data.worstDevice.result!!.toInt())
                val bestMemMode = BorderlineDevice(state.data.bestDeviceMem.device, state.data.bestDeviceMem.result!!.toInt())
                val worstMemMode = BorderlineDevice(state.data.worstDeviceMem.device, state.data.worstDeviceMem.result!!.toInt())
                borderlineDeviceStorage.add(bestProcMode)
                borderlineDeviceStorage.add(worstProcMode)
                borderlineDeviceStorage.add(bestMemMode)
                borderlineDeviceStorage.add(worstMemMode)
            }
            is Loading -> {
            }
            is Error -> {
                showSnackMessage("Что - то пошло не так")
            }
        }
    }

    fun startButtonEnabeled(){
        if (runWallTask != null) {
            runWallTask.isEnabled = true
        }
    }

    fun beginTest(view: View, mode: Modes, wallChart: WallTestChart, chartConsumptionGraph: BarChart){
        if (firstRunning) {
            descriptionCardOut()
            firstRunning = false
        }
        else{
            resOut()
        }
        pbIn()
        viewModel.runTask(view, mode, borderlineDeviceStorage, wallChart, chartConsumptionGraph, this.context!!, ::chartIn,
            ::startButtonEnabeled, ::pbOut, ::resIn)
    }


    // animations segments
    fun chartIn(){
        if (WallCardChart != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation)
            WallCardChart.startAnimation(animation)
            WallCardChart.visibility = View.VISIBLE
        }
    }


    fun chartOut(){
        if (WallCardChart != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation_out)
            WallCardChart.startAnimation(animation)
            WallCardChart.visibility = View.INVISIBLE
        }
    }

    fun descriptionCardIn(){
        if (WallDescripCardView != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation)
            WallDescripCardView .startAnimation(animation)
            WallDescripCardView .visibility = View.VISIBLE
        }
    }

    fun descriptionCardOut(){
        if (WallDescripCardView != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation_out)
            WallDescripCardView .startAnimation(animation)
            WallDescripCardView .visibility = View.INVISIBLE
        }
    }

    fun pbIn(){
        if (WallProgrCardView != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation)
            WallProgrCardView.startAnimation(animation)
            WallProgrCardView.visibility = View.VISIBLE
        }
    }

    fun pbOut(){
        if (WallProgrCardView != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation_out)
            WallProgrCardView.startAnimation(animation)
            WallProgrCardView.visibility = View.INVISIBLE
        }
    }

    fun resIn(){
        if (WallResultCardView != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation)
            WallResultCardView.startAnimation(animation)
            WallResultCardView.visibility = View.VISIBLE
        }
    }

    fun resOut(){
        if (WallResultCardView != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation_out)
            WallResultCardView.startAnimation(animation)
            WallResultCardView.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyCoroutines()
        firstRunning = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.destroyCoroutines()
        firstRunning = true
        viewModel.getCoroutineInform() // check coroutin canceliing
    }
}