package com.example.android.roomdevice

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DeviceInformation")
data class Device(@PrimaryKey @ColumnInfo(name = "deviceID") val deviceID: Int,
                  @ColumnInfo(name = "Device") val device: String,
                  @ColumnInfo(name = "BrickPerfomance") var brickPerfomance: Int,
                  @ColumnInfo(name = "LengthPerfomance") var lengthPerfomance: Double,
                  @ColumnInfo(name = "SystemParameter") var systemParameter: String)

