package com.example.maapp.presentation.ui.device

import android.os.Bundle
import android.view.View
import com.example.maapp.CustomTestsFragments.ResultsTestDialog
import com.example.maapp.R
import com.example.maapp.data.entity.DeviceInformation
import com.example.maapp.presentation.system.argument
import com.example.maapp.presentation.ui.base.BaseFragment
import github.nisrulz.easydeviceinfo.base.*
import kotlinx.android.synthetic.main.fragment_device_info.*

class DeviceInfoFragment : BaseFragment() {

    companion object {
        private const val ARG_DEVICE = "ARG_DEVICE"

        fun create(device: DeviceInformation) =
            DeviceInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_DEVICE, device)
                }
            }
    }


    override val layoutRes: Int = R.layout.fragment_device_info

    private val stringBuffer = StringBuffer()

    val device : DeviceInformation? by argument(ARG_DEVICE)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(device)
    }


    private fun initUi(device : DeviceInformation?) {
        if(device == null) return

        tv_brand.text = device.brand
        tv_model.text = device.model
        tv_os.text = device.os
        tv_cpu.text = device.cpu
        tv_ram.text = ((device.ram.toString().toLong() / 1024L / 1024L ).toString()) + " MB"
        //tv_memory.text = ((device.memory.toString().toLong() / 1024L / 1024L).toString()) + " MB"

        toolbar_device_info.title = device.model
        configureToolbar(toolbar_device_info)
    }

    private fun easyDevice() {
        val easySensor = EasySensorMod(requireContext())
        easySensor.allSensors.forEach {
            with(it){
                stringBuffer.append("Vendor = $vendor").append("\n")
                stringBuffer.append("Version = $version").append("\n")
                stringBuffer.append("Power = $power").append("\n")
                stringBuffer.append("Resolution = $resolution").append("\n")
                stringBuffer.append("MaxRange = $maximumRange").append("\n")
                stringBuffer.append("Name = $name").append("\n\n")
            }
        }

        val easyFingerprintMod = EasyFingerprintMod(requireContext())
        stringBuffer.append("easyFingerprintMod = ${easyFingerprintMod.isFingerprintSensorPresent}").append("\n\n")

        val easyNetwork = EasyNetworkMod(requireContext())
        stringBuffer.append("iPv4Address = ${easyNetwork.iPv4Address}").append("\n")
        stringBuffer.append("iPv6Address = ${easyNetwork.iPv6Address}").append("\n")
        stringBuffer.append("wifiBSSID = ${easyNetwork.wifiBSSID}").append("\n\n")

        val easyMemoryMod = EasyMemoryMod(requireContext())
        stringBuffer.append("totalExternalMemorySize = ${easyMemoryMod.totalExternalMemorySize}").append("\n")
        stringBuffer.append("totalInternalMemorySize = ${easyMemoryMod.totalInternalMemorySize}").append("\n")
        stringBuffer.append("totalRAM = ${easyMemoryMod.totalRAM}").append("\n")
        stringBuffer.append("availableExternalMemorySize = ${easyMemoryMod.availableExternalMemorySize}").append("\n")
        stringBuffer.append("availableInternalMemorySize = ${easyMemoryMod.availableInternalMemorySize}").append("\n\n")

        val easyBatteryMod = EasyBatteryMod(requireContext())
        stringBuffer.append("batteryTechnology = ${easyBatteryMod.batteryTechnology}").append("\n")
        stringBuffer.append("batteryVoltage = ${easyBatteryMod.batteryVoltage}").append("\n\n")

        val easyCpuMod = EasyCpuMod()
        stringBuffer.append("stringSupported32bitABIS = ${easyCpuMod.stringSupported32bitABIS}").append("\n")
        stringBuffer.append("stringSupported64bitABIS = ${easyCpuMod.stringSupported64bitABIS}").append("\n")
        stringBuffer.append("stringSupportedABIS = ${easyCpuMod.stringSupportedABIS}").append("\n\n")

        val easyDeviceMod = EasyDeviceMod(requireContext())
        stringBuffer.append("Screen Display ID = ${easyDeviceMod.screenDisplayID}").append("\n")
        stringBuffer.append("Manufacturer = ${easyDeviceMod.manufacturer}").append("\n")
        stringBuffer.append("Model = ${easyDeviceMod.model}").append("\n")
        stringBuffer.append("OS Code Name = ${easyDeviceMod.osCodename}").append("\n")
        stringBuffer.append("OS Version = ${easyDeviceMod.osVersion}").append("\n")
        stringBuffer.append("Radio = ${easyDeviceMod.radioVer}").append("\n")
        stringBuffer.append("Product = ${easyDeviceMod.product}").append("\n")
        stringBuffer.append("Device = ${easyDeviceMod.device}").append("\n")
        stringBuffer.append("Board = ${easyDeviceMod.board}").append("\n")
        stringBuffer.append("Hardware = ${easyDeviceMod.hardware}").append("\n")
        stringBuffer.append("Bootloader = ${easyDeviceMod.bootloader}").append("\n")
        stringBuffer.append("Fingerprint = ${easyDeviceMod.fingerprint}").append("\n")
        stringBuffer.append("buildBrand = ${easyDeviceMod.buildBrand}").append("\n")
        stringBuffer.append("buildHost = ${easyDeviceMod.buildHost}").append("\n")
        stringBuffer.append("buildTags = ${easyDeviceMod.buildTags}").append("\n")
        stringBuffer.append("buildVersionRelease = ${easyDeviceMod.buildVersionRelease}").append("\n")

    }
}