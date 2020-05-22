package com.example.maapp.TableObjects

import com.google.gson.annotations.SerializedName

data class BandWidth (
    @SerializedName("name") var tableName: String,
    @SerializedName("device") var device: String,
    @SerializedName("result") var testResult: Double)
