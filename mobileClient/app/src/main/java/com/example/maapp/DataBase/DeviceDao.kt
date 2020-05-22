package com.example.android.roomdevice

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.annotation.GuardedBy
import androidx.annotation.StringRes

@Dao
interface DeviceDao {

    @Query("SELECT * from DeviceInformation ORDER BY brickPerfomance DESC")
    fun getDevicesRecord(): LiveData<List<Device>>

    @Query("SELECT * from DeviceInformation WHERE Device LIKE '%' || :searchString || '%'")
    fun getSomeDevicesRecord(searchString: String): LiveData<List<Device>>

    @Query("SELECT * from DeviceInformation limit 1")
    fun getTopRecord(): Device

    @Query("SELECT * from DeviceInformation ORDER BY lengthPerfomance ASC limit 1")
    fun getWorstRecord() : Device


    @Insert
    fun insert(device: Device)

    @Query("DELETE FROM DeviceInformation")
    fun deleteAll()
}
