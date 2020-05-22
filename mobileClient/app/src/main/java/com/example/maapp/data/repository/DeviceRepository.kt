package com.example.maapp.data.repository

import com.example.maapp.data.entity.*
import com.example.maapp.data.network.DeviceTestApi
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class DeviceRepository @Inject constructor(
        private val deviceTestApi: DeviceTestApi
) {
    fun getDevicesWallProc() : Single<RequestResult> {
        return deviceTestApi.getDevicesWallProc()
    }

    fun getDevicesWallMem() : Single<RequestResult> {
        return deviceTestApi.getDevicesWallMem()
    }

    fun getDevicesBandwidthProc() : Single<RequestResult> {
        return deviceTestApi.getDevicesBandwidthProc()
    }

    fun getDevicesBandwidthMem() : Single<RequestResult> {
        return deviceTestApi.getDevicesBandwidthMem()
    }

    fun getDevicesSearchProc() : Single<RequestResult> {
        return deviceTestApi.getDevicesSearchProc()
    }

    fun getDevicesSearchMem() : Single<RequestResult> {
        return deviceTestApi.getDevicesSearchMem()
    }

    fun getBorderlineDevices() : Single<RequestTestResult> {
        return deviceTestApi.getBorderlineDevicesWall()
    }

    fun sendDevice(response: DeviceResponse) : Completable {
        return deviceTestApi.sendWallTestProcResult(response)
    }

    fun sendWallTestMemResult(response: DeviceResponse) : Completable {
        return deviceTestApi.sendWallTestMemResult(response)
    }



    fun getBorderlineDevicesBandwidth() : Single<RequestTestResult> {
        return deviceTestApi.getBorderlineDevicesBandwidth()
    }

    fun sendBandwidthTestProcResult(response: DeviceResponse) : Completable{
        return deviceTestApi.sendBandwidthProcResult(response)
    }

    fun sendBandwidthTestMemResult(response: DeviceResponse) : Completable{
        return deviceTestApi.sendBandwidthMemResult(response)
    }



    fun getBorderlineDevicesSearch() : Single<RequestTestResult>{
        return deviceTestApi.getBorderlineDevicesSearch()
    }

    fun sendSearchTestProcResult(response: DeviceResponse) : Completable{
        return deviceTestApi.sendSearchProcResult(response)
    }

    fun sendSearchTestMemResult(response: DeviceResponse) : Completable{
        return deviceTestApi.sendSearchMemResult(response)
    }
}