package com.aconno.sensorics.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ConnectionWriteModel(
    @SerializedName("serviceUUID")
    @Expose
    val serviceUUID: String,
    @SerializedName("characteristicUUID")
    @Expose
    val characteristicUUID: String,
    @SerializedName("characteristicName")
    @Expose
    val characteristicName: String,
    @SerializedName("values")
    @Expose
    val values: List<ValueModel>
)