package com.example.maapp.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeviceInformation(
    @SerializedName("Brand")
    val brand: String? = null,
    @SerializedName("Model")
    val model: String? = null,
    @SerializedName("os")
    val os: String? = null,
    @SerializedName("cpu")
    val cpu: String? = null,
    @SerializedName("ram")
    val ram: String? = null,
    @SerializedName("memory")
    val memory : String? = null
) : Parcelable