package com.example.maapp.presentation.ui.leaderboard

import androidx.lifecycle.MutableLiveData
import com.example.maapp.data.entity.DeviceResponse
import com.example.maapp.data.entity.RequestResult
import com.example.maapp.data.repository.DeviceRepository
import com.example.maapp.presentation.system.onNext
import com.example.maapp.presentation.system.schedulersIoToMain
import com.example.maapp.presentation.ui.base.state.Data
import com.example.maapp.presentation.ui.base.state.Loading
import com.example.maapp.presentation.ui.base.state.Error
import com.example.maapp.presentation.ui.base.state.ViewState
import com.example.maapp.presentation.ui.base.view_model.BaseViewModel
import com.example.maapp.presentation.ui.leaderboard.adapter.SelectTest
import com.example.maapp.presentation.ui.tests.Modes
import timber.log.Timber
import javax.inject.Inject

class LeaderBoardViewModel @Inject constructor(
        private val repository: DeviceRepository
) : BaseViewModel() {

    val devices : MutableLiveData<ViewState<RequestResult>> = MutableLiveData()

    init {
        repository.getDevicesWallProc()
                .schedulersIoToMain()
                .toObservable()
                .map<ViewState<RequestResult>> {
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

    fun refresh(testState: SelectTest, mode: Modes){
        if (mode.name == "PROCESSOR") {
            when(testState){
                SelectTest.WALL_TEST -> {
                    repository.getDevicesWallProc()
                        .schedulersIoToMain()
                        .toObservable()
                        .map<ViewState<RequestResult>> {
                            Data(it)
                        }
                        .startWith(Loading)
                        .onErrorReturn(::Error)
                        .subscribe({
                            devices.onNext(it)
                        }, {
                            Timber.d(it)
                        }).autoDispose()
                }
                SelectTest.BANDWIDTH_TEST -> {
                    repository.getDevicesBandwidthProc()
                        .schedulersIoToMain()
                        .toObservable()
                        .map<ViewState<RequestResult>> {
                            Data(it)
                        }
                        .startWith(Loading)
                        .onErrorReturn(::Error)
                        .subscribe({
                            devices.onNext(it)
                        }, {
                            Timber.d(it)
                        }).autoDispose()
                }
                SelectTest.SEARCH_TEST -> {
                    repository.getDevicesSearchProc()
                        .schedulersIoToMain()
                        .toObservable()
                        .map<ViewState<RequestResult>> {
                            Data(it)
                        }
                        .startWith(Loading)
                        .onErrorReturn(::Error)
                        .subscribe({
                            devices.onNext(it)
                        }, {
                            Timber.d(it)
                        }).autoDispose()
                }
            }
        }
        else {
            when(testState){
                SelectTest.WALL_TEST -> {
                    repository.getDevicesWallMem()
                        .schedulersIoToMain()
                        .toObservable()
                        .map<ViewState<RequestResult>> {
                            Data(it)
                        }
                        .startWith(Loading)
                        .onErrorReturn(::Error)
                        .subscribe({
                            devices.onNext(it)
                        }, {
                            Timber.d(it)
                        }).autoDispose()
                }
                SelectTest.BANDWIDTH_TEST -> {
                    repository.getDevicesBandwidthMem()
                        .schedulersIoToMain()
                        .toObservable()
                        .map<ViewState<RequestResult>> {
                            Data(it)
                        }
                        .startWith(Loading)
                        .onErrorReturn(::Error)
                        .subscribe({
                            devices.onNext(it)
                        }, {
                            Timber.d(it)
                        }).autoDispose()
                }
                SelectTest.SEARCH_TEST -> {
                    repository.getDevicesSearchMem()
                        .schedulersIoToMain()
                        .toObservable()
                        .map<ViewState<RequestResult>> {
                            Data(it)
                        }
                        .startWith(Loading)
                        .onErrorReturn(::Error)
                        .subscribe({
                            devices.onNext(it)
                        }, {
                            Timber.d(it)
                        }).autoDispose()
                }
            }
        }
    }
}