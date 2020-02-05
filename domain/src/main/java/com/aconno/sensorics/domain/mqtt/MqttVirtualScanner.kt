package com.aconno.sensorics.domain.mqtt

import com.aconno.sensorics.domain.model.Device
import com.aconno.sensorics.domain.model.ScanResult
import com.aconno.sensorics.domain.virtualscanningsources.mqtt.MqttVirtualScanningSource
import io.reactivex.Flowable

interface MqttVirtualScanner {
    fun startScanning(devices: List<Device> = listOf())
    fun addSource(serverUri: String, clientId: String)
    fun stopScanning()
    fun getScanResults(): Flowable<ScanResult>
    fun removeSource(serverUri: String, clientId: String?)
    fun addDevicesToScanFor(devices: List<Device>)
    fun addDeviceToScanFor(device: Device)
    fun removeDevicesToScanFor(devices: List<Device>)
    fun removeDeviceToScanFor(device: Device)
    fun clearSources()

    fun testConnection(testConnectionCallback: TestConnectionCallback,mqttVirtualScanningSource: MqttVirtualScanningSource)

    interface TestConnectionCallback {
        fun onConnectionStart()
        fun onConnectionSuccess()
        fun onConnectionFail(exception: Throwable?)
    }
}