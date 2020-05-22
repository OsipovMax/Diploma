package com.example.maapp.data.entity

data class DeviceResponse(
        val device: String?,
        val deviceInformation: DeviceInformation?,
        //val id: Int?,
        val result: Double?
)

data class RequestResult(
        val bestDevice: DeviceResponse,
        val worstDevice: DeviceResponse,
        val devicesList: List<DeviceResponse>
)

data class RequestTestResult(
        val bestDevice: DeviceResponse,
        val worstDevice: DeviceResponse,
        val bestDeviceMem: DeviceResponse,
        val worstDeviceMem: DeviceResponse
)


data class TestResult(
        val id : Int?,
        val device: String?,
        val result: Int?,
        val deviceInformation: DeviceInformation?
)
