package com.example.maapp.presentation.ui.leaderboard

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.maapp.R
import com.example.maapp.data.entity.DeviceInformation
import com.example.maapp.data.entity.DeviceResponse
import com.example.maapp.data.entity.RequestResult
import com.example.maapp.presentation.system.getViewModel
import com.example.maapp.presentation.system.observe
import com.example.maapp.presentation.ui.base.BaseFragment
import com.example.maapp.presentation.ui.base.state.Data
import com.example.maapp.presentation.ui.base.state.Loading
import com.example.maapp.presentation.ui.base.state.ViewState
import com.example.maapp.presentation.ui.base.view_model.showSnackMessage
import com.example.maapp.presentation.ui.device.DeviceInfoFragment
import com.example.maapp.presentation.ui.leaderboard.adapter.DeviceItem
import com.example.maapp.presentation.ui.leaderboard.adapter.SelectTest
import com.example.maapp.presentation.ui.tests.Modes
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_leader_board.*
import java.lang.Error
import java.util.*
import javax.inject.Inject
import javax.inject.Provider


class LeaderboardFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.activity_leader_board
    private lateinit var groupieAdapter: GroupAdapter<GroupieViewHolder>

    @Inject
    lateinit var viewModelProvider: Provider<LeaderBoardViewModel>
    private val viewModel by lazy { getViewModel(viewModelProvider) }
    var testState : SelectTest = SelectTest.WALL_TEST
    var testMode : Modes = Modes.PROCESSOR



    override fun onAttach(context: Context) {
        appComponent.inject(this@LeaderboardFragment)
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observe(viewModel.devices, ::updateDevicesList)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.leader_board_menu, menu)
        menu.findItem(R.id.wall).isChecked = true
        menu.findItem(R.id.processor_mode).isChecked = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.processor_mode -> {
                item.isChecked = true
                testMode = Modes.PROCESSOR
                viewModel.refresh(testState, testMode)
            }
            R.id.memory_mode -> {
                item.isChecked = true
                testMode = Modes.MEMORY
                viewModel.refresh(testState, testMode)
            }

            R.id.wall -> {
                item.isChecked = true
                testState = SelectTest.WALL_TEST
                toolbar_dashboard.title = "Результаы(метр) - \"Китайская стена\""
                val snackbar = Snackbar.make(
                    view!!, "Список лучших устройств по тесту \"Китайская стена\".",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackbar.show()
                viewModel.refresh(testState, testMode)
            }

            R.id.bandwidth -> {
                item.isChecked = true
                testState = SelectTest.BANDWIDTH_TEST
                toolbar_dashboard.title = "Результат(авто) - \"Пропускная способность\""
                val snackbar = Snackbar.make(
                    view!!, "Список лучших устройств по тесту \"Пропускная способность\".",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackbar.show()
                viewModel.refresh(testState, testMode)
            }

            R.id.search -> {
                item.isChecked = true
                testState = SelectTest.SEARCH_TEST
                toolbar_dashboard.title = "Результат(сравнений) - \"Поиск\""
                val snackbar = Snackbar.make(
                    view!!, "Список лучших устройств по тесту \"Поиск\".",
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackbar.show()
                viewModel.refresh(testState, testMode)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        testState = SelectTest.WALL_TEST
        testMode = Modes.PROCESSOR
        viewModel.refresh(testState, testMode)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureToolbar(toolbar_dashboard)
        toolbar_dashboard.title = "Результат(метр) - \"Китайская стена\""
        groupieAdapter = GroupAdapter()
        with(rv_device) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groupieAdapter
        }
    }

    private fun updateDevicesList(state: ViewState<RequestResult>){
        when (state) {
            is Data -> {
                val deviceClick : (DeviceInformation) -> Unit = {
                    fragmentNavigation.pushFragment(DeviceInfoFragment.create(it))
                }
                var ratingNum = 0
                val devicesItems = state.data.devicesList.map {
                    ratingNum++
                    DeviceItem(state.data.bestDevice,
                    state.data.worstDevice, it, ratingNum, deviceClick)}
                    groupieAdapter.update(devicesItems)
            }
            is Loading -> {
            }
            is Error -> {
                showSnackMessage("Что - то пошло не так")
            }
        }
    }
}