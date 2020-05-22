package com.example.android.roomdevice

import androidx.lifecycle.LiveData
import androidx.annotation.WorkerThread

class DeviceRepository(private val deviceDao: DeviceDao, private var searchDevice: String) {

    val allDevices: LiveData<List<Device>> = deviceDao.getDevicesRecord()

    val someDevices: LiveData<List<Device>> = deviceDao.getSomeDevicesRecord(searchDevice)


    @Suppress
    @WorkerThread
    suspend fun getTop() : Device{
        val topDevice: Device = deviceDao.getTopRecord()
        return topDevice
    }

    @Suppress
    @WorkerThread
    suspend fun getWorst() : Device{
        val worstDevice: Device = deviceDao.getWorstRecord()
        return worstDevice
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(device: Device) {
        deviceDao.insert(device)
    }
}
