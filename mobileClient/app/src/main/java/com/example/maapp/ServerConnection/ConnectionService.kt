package com.example.maapp.ServerConnection

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import com.example.maapp.TableObjects.BandWidth
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.ConnectException
import java.net.Socket
import java.net.SocketException

class ConnectionService : Service() {
    private val port = 12333
    private var soc = Socket()
    private val myBinder = MyLocalBinder()
    var localStorage: String = String()

    inner class MyLocalBinder: Binder() {
        fun getService(): ConnectionService {
            return this@ConnectionService
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return myBinder
    }


    fun conn():Boolean{
        println("Connect")
        try {
            //soc = Socket("93.180.9.72", port)
            soc = Socket("192.168.1.167", 55577)
        }
        catch (e : ConnectException) {
            return false
        }
        return true
    }

    fun disconn(){
        println("Disconnect")
        soc.close()
    }

    fun getDeviceName(): String{
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        if (model.startsWith(manufacturer)) {
            return capitalize(model)
        }
        else {
            return capitalize(manufacturer) + " " + model
        }
    }

    fun capitalize(stringRepresentation: String): String{
        if (stringRepresentation == null) {
            return "UUU"
        }
        return stringRepresentation
    }

    fun getMemoryInfo(): Long {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memory = activityManager.memoryClass
        val memoryInfo = ActivityManager.MemoryInfo()
        //println("total memory is ${memoryInfo.totalMem} B")
        //println("available memory is  ${memoryInfo.availMem / 1024 / 1024} MB")
        //println("threshold memory is ${memoryInfo.threshold / 1024 / 1024} MB")
        return memoryInfo.totalMem
    }
    fun dataPacking(perfom: ArrayList<Double> ): ByteArray{
        var generalStringData = ""
        val device = getDeviceName()
        val tableFlag = perfom[0]
        val testResult = perfom[1]
        val brickResult = perfom[2].toInt()
        val ram = getMemoryInfo().toString()

        if (tableFlag == 0.0) {
            generalStringData = "W$device/$brickResult/$testResult/$ram GB RAM/"
        }
        else if (tableFlag == 1.0) {
            //val gson = Gson()
            //val json = gson.toJson(BandWidth("B",device,testResult))
            //print("HELLO BLE")
            generalStringData = "B$device/$testResult/"
            //return json.toByteArray(Charsets.UTF_8)
        }
        else if (tableFlag == 2.0) {
            generalStringData = "S$device/$testResult/"
        }
        val charSet = Charsets.UTF_8
        val byteArray = generalStringData.toByteArray(charSet) //???
        return byteArray
    }

    fun sendRecordToServ(perfom: ArrayList<Double>){
        //println("message - $perfom")
        val messageToSend = dataPacking(perfom)
        GlobalScope.launch {
            try {
                val writer = soc.getOutputStream()
                writer.write(messageToSend)
                writer.flush()

                delay(100)
                disconn()
                conn()
                getDeviceList()
            }
            catch (e: SocketException){
                println("Отсутствует подключение к интернет")
            }
        }
    }

    fun getDeviceList(){
        var mStr = String()
        val charset = Charsets.UTF_8
        val recieveBuffer : ByteArray = ByteArray(4096)
        println("message - receive")
        GlobalScope.launch {
            delay(1000)
            val reader = soc.getInputStream()
            reader.read(recieveBuffer)
            mStr = recieveBuffer.toString(charset)
            println(mStr)
            localStorage = mStr
            println(localStorage)
        }
    }

    fun getFile() {
        val din = DataInputStream(soc.getInputStream())
        val byteArray = ByteArray(8192)
        var file = File("sDb")
        if(!file.exists()){
            file.createNewFile()
            println("Created new File: ")
        }
        val fos = FileOutputStream(file)
        var counter = 0
        while(counter >=0) {
            counter = din.read(byteArray, 0, byteArray.size)
            fos.write(byteArray,0,counter)
        }
        fos.flush()
        fos.close()
    }

}
