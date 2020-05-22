package com.example.maapp.presentation.ui.tests.searchTest

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import com.example.maapp.CustomTestsFragments.DescriptionTestDialog
import com.example.maapp.R
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_search_test.*
import java.lang.Error
import javax.inject.Inject
import javax.inject.Provider

class SearchTestFragment: BaseFragment() {
    override val layoutRes: Int = R.layout.activity_search_test
    @Inject
    lateinit var viewModelProvider: Provider<SearchTestViewModel>
    private val viewModel by lazy { getViewModel(viewModelProvider) }
    private var borderlineDeviceStorage : ArrayList<BorderlineDevice> = arrayListOf()
    lateinit var  runTask : TaskRunning
    private var mode = Modes.PROCESSOR
    private var firstRunning = true
    var wallChart = WallTestChart()


    override fun onAttach(context: Context) {
        appComponent.injectSearchTest(this@SearchTestFragment)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(viewModel.devices, ::updateBestWorstDeivces)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tests_menu, menu)
        val firstItem = menu.findItem(R.id.proc)
        val secondItem = menu.findItem(R.id.mem)
        firstItem.setTitle("Сравнение с эталоном")
        secondItem.setTitle("Сравнение каждого с каждым")
        menu.findItem(R.id.proc).isChecked = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.proc -> {
                item.isChecked = true
                val snackbar = Snackbar.make(
                    view!!, "Сравнение с эталоном.",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackbar.show()
                mode = Modes.PROCESSOR
            }
            R.id.mem -> {
                item.isChecked = true
                val snackbar = Snackbar.make(
                    view!!, "Сравнение каждого с каждым",
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
        configureToolbar(search_test_tb)
        search_test_tb.title = "Тест - \"Поиск\""
        runTask = TaskRunning()

        aboutSearchTest.setOnClickListener{
            val sheetTag = "someTag"
            val temp : ArrayList<String?> = ArrayList(1)
            temp.add("3")
            val descriptionBottomSheet = DescriptionTestDialog.create(temp)
            descriptionBottomSheet.show(activity!!.supportFragmentManager, sheetTag)
        }

        runSearchTask.setOnClickListener {
            beginTest(view, mode, wallChart, SearchChartConsumptionGraph)
        }

        wallChart.populateGraphData(0, 0, 0, SearchChartConsumptionGraph, "search")
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
        if (runSearchTask != null) {
            runSearchTask.isEnabled = true
        }
    }

    fun beginTest(view: View, mode: Modes, wallChart: WallTestChart, SearchChartConsumptionGraph: BarChart){
        if (firstRunning) {
            descriptionCardOut()
            firstRunning = false
        }
        else{
            resOut()
        }
        pbIn()
        viewModel.runTask(view, mode, borderlineDeviceStorage, wallChart, SearchChartConsumptionGraph, this.context!!, ::chartIn,
            ::startButtonEnabeled, ::pbOut, ::resIn)
    }

    fun chartIn(){
        if (SearchCardChart != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation)
            SearchCardChart.startAnimation(animation)
            SearchCardChart.visibility = View.VISIBLE
        }
    }

    fun descriptionCardIn(){
        if (SearchDescripCardView != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation)
            SearchDescripCardView.startAnimation(animation)
            SearchDescripCardView.visibility = View.VISIBLE
        }
    }

    fun descriptionCardOut(){
        if (SearchDescripCardView != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation_out)
            SearchDescripCardView.startAnimation(animation)
            SearchDescripCardView.visibility = View.INVISIBLE
        }
    }

    fun pbIn(){
        if (SearchProgrCardView != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation)
            SearchProgrCardView.startAnimation(animation)
            SearchProgrCardView.visibility = View.VISIBLE
        }
    }

    fun pbOut(){
        if (SearchProgrCardView != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation_out)
            SearchProgrCardView.startAnimation(animation)
            SearchProgrCardView.visibility = View.INVISIBLE
        }
    }

    fun resIn(){
        if (SearchResultCardView != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation)
            SearchResultCardView.startAnimation(animation)
            SearchResultCardView.visibility = View.VISIBLE
        }
    }

    fun resOut(){
        if (SearchResultCardView != null) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.chart_animation_out)
            SearchResultCardView.startAnimation(animation)
            SearchResultCardView.visibility = View.INVISIBLE
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