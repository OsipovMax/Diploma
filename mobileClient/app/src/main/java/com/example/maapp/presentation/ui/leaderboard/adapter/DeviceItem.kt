package com.example.maapp.presentation.ui.leaderboard.adapter

import android.graphics.Typeface
import android.os.Build
import com.example.maapp.R
import com.example.maapp.data.entity.DeviceInformation
import com.example.maapp.data.entity.DeviceResponse
import com.example.maapp.data.entity.RequestResult
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_device.view.*
import java.lang.reflect.Type

class DeviceItem(
        private val bestDevice: DeviceResponse,
        private val worstDevice: DeviceResponse,
        private val device : DeviceResponse,
        private var ratingPlace: Int,
        private val clickDeviceListener : (DeviceInformation) -> Unit
) : Item() {


    fun deviceDefinition() : String{
        var ownDevice = ""
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        ownDevice = manufacturer + " " + model
        return ownDevice
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tv_device_name.text = device.device.orEmpty()
        val ownDevice = deviceDefinition()
        if (ownDevice == device.device) {
            viewHolder.itemView.tv_device_name.setTypeface(null, Typeface.BOLD)
        }
        else {
            viewHolder.itemView.tv_device_name.setTypeface(null, Typeface.NORMAL)
        }
        //viewHolder.itemView.tv_rating_place.text = device.id.toString()
        viewHolder.itemView.tv_rating_place.text = ratingPlace.toString()
        //ratingPlace += 1
        device.result!!.toFloat().let {
            viewHolder.itemView.test_result_indicator.setIndicatorProgress(it)
        }
        bestDevice.result!!.toFloat().let {
            viewHolder.itemView.test_result_indicator.setMaxValue(it)
        }
        viewHolder.itemView.tv_test_result.text = device.result.toInt().toString()
        viewHolder.itemView.setOnClickListener{
            device.deviceInformation?.let {
                clickDeviceListener.invoke(it)
            }
        }
    }
    override fun getLayout(): Int = R.layout.item_device
}