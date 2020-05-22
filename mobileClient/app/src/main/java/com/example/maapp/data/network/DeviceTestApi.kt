package com.example.maapp.data.network

import com.example.maapp.data.entity.*
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface DeviceTestApi {

    //------------------------------leaderboards------------------------
    @GET("/leaderBoard/Wall/Proc")
    fun getDevicesWallProc() : Single<RequestResult>

    @GET("leaderBoard/Wall/Mem")
    fun getDevicesWallMem() : Single<RequestResult>

    @GET("/leaderBoard/Bandwidth/Proc")
    fun getDevicesBandwidthProc() : Single<RequestResult>

    @GET("/leaderBoard/Bandwidth/Mem")
    fun getDevicesBandwidthMem() : Single<RequestResult>

    @GET("/leaderBoard/Search/Proc")
    fun getDevicesSearchProc() : Single<RequestResult>

    @GET("leaderBoard/Search/Mem")
    fun getDevicesSearchMem() : Single<RequestResult>

    //----------------------------------------------------------------------------------

    @GET("/Wall")
    fun getBorderlineDevicesWall() : Single<RequestTestResult>

    @POST("/Wall/Proc") // /wallTest/Mem
    fun sendWallTestProcResult(@Body response: DeviceResponse) : Completable

    @POST("/Wall/Mem")
    fun sendWallTestMemResult(@Body response: DeviceResponse) : Completable

    //----------------------------------------------------------------------------------

    @GET("/Bandwidth")
    fun getBorderlineDevicesBandwidth() : Single<RequestTestResult>

    @POST("/Bandwidth/Proc")
    fun sendBandwidthProcResult(@Body response: DeviceResponse) : Completable

    @POST("/Bandwidth/Mem")
    fun sendBandwidthMemResult(@Body response: DeviceResponse) : Completable

    //----------------------------------------------------------------------------------

    @GET("/Search")
    fun getBorderlineDevicesSearch() : Single<RequestTestResult>

    @POST("/Search/Proc")
    fun sendSearchProcResult(@Body response: DeviceResponse) : Completable

    @POST("/Search/Mem")
    fun sendSearchMemResult(@Body response: DeviceResponse) : Completable
}