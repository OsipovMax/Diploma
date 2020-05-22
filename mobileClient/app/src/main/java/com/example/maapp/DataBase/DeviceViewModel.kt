package com.example.android.roomdevice


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext



class DeviceViewModel(application: Application) : AndroidViewModel(application) {
    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private var repository: DeviceRepository? = null

    var allDevices: LiveData<List<Device>> ? = null

    init {
      /*  val deviceDao = DeviceRoomDatabase.getDatabase(application, scope,"").deviceDao()
        repository = DeviceRepository(deviceDao)
        allDevices = repository.allDevices*/
    }

    fun firstLoadData (application: Application, str:String) {
        val deviceDao = DeviceRoomDatabase.getDatabase(application, scope, str).deviceDao()
        repository = DeviceRepository(deviceDao,"")
        allDevices = repository?.allDevices
    }

    fun showSearchResults(application: Application, searchString: String){
        val deviceDao = DeviceRoomDatabase.getLocalDatabase(application, scope).deviceDao()
        repository = DeviceRepository(deviceDao,searchString)
        allDevices = repository?.someDevices
    }
    fun showLocalData(application: Application){
        val deviceDao = DeviceRoomDatabase.getLocalDatabase(application, scope).deviceDao()
        repository = DeviceRepository(deviceDao,"")
        allDevices = repository?.allDevices
    }


    fun insert(device: Device) = scope.launch(Dispatchers.IO) {
        repository?.insert(device)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
