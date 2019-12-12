package com.aconno.sensorics.ui.settings_framework

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.aconno.bluetooth.beacon.Slot.Companion.EXTRA_BEACON_SLOT_POSITION
import com.aconno.sensorics.R
import com.aconno.sensorics.device.beacon.Beacon
import com.aconno.sensorics.device.beacon.Slot
import com.aconno.sensorics.device.beacon.Slots
import com.aconno.sensorics.model.javascript.SlotJS
import com.aconno.sensorics.ui.configure.ViewPagerSlider
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_beacon_general2.*
import timber.log.Timber

open class BeaconSettingsSlotFragment : Fragment() {

    private val beaconViewModel: BeaconSettingsViewModel by lazy {
        ViewModelProviders.of(requireActivity()).get(BeaconSettingsViewModel::class.java)
    }

    private lateinit var beacon: Beacon

    private lateinit var slots: Slots

    private var slotPosition: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        arguments?.let {
            slotPosition = it.getInt(EXTRA_BEACON_SLOT_POSITION)

            beaconViewModel.beacon.value?.let { beacon ->
                this.beacon = beacon
                this.slots = beacon.slots

            } ?: throw IllegalStateException(
                "Started BeaconSlotFragment without beacon!"
            )
        } ?: throw IllegalStateException(
            "Started BeaconSlotFragment without EXTRA_BEACON_SLOT_POSITION argument!"
        )
        return inflater.inflate(R.layout.fragment_beacon_general2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null)
            webview_general.restoreState(savedInstanceState)
        initiateWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initiateWebView() {
        webview_general.settings.javaScriptEnabled = true
        webview_general.addJavascriptInterface(WebAppInterface(), "Android")
        webview_general.webViewClient = WebAppClient()
        webview_general.loadUrl(HTML_FILE_PATH)
    }


    //Prevent running twice or more
    inner class WebAppClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            if (isAdded) {
                getSlotJson()?.let {
                    callJavaScript("init", it)
                }
            }
        }
    }

    private fun callJavaScript(methodName: String, vararg params: Any) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("javascript:try{")
        stringBuilder.append(methodName)
        stringBuilder.append("(")
        for (i in params.indices) {
            val param = params[i]
            if (param is String) {
                stringBuilder.append("'")
                stringBuilder.append(param.toString().replace("'", "\\'"))
                stringBuilder.append("'")
            }
            if (i < params.size - 1) {
                stringBuilder.append(",")
            }
        }
        stringBuilder.append(")}catch(error){Android.onError(error.message);}")
        webview_general.loadUrl(stringBuilder.toString())
    }

    private fun getSlotJson(): String? {
        val slotPosition = arguments!!.getInt(EXTRA_BEACON_SLOT_POSITION)

        val data = beaconViewModel.beacon.value?.slots?.get(slotPosition)?.let {
            SlotJS(
                it.getType().tabName,//Do not change the order
                it.advertisingContent,
                it.name,
                when (it.advertisingMode) {
                    Slot.AdvertisingModeParameters.Mode.INTERVAL -> false
                    Slot.AdvertisingModeParameters.Mode.EVENT -> true
                },
                it.packetCount,
                beacon.supportedTxPowers,
                beacon.supportedTxPowers.indexOf(it.txPower),
                it.readOnly
            )
        }?.let {
            convertKeysToJavascriptFormat(Gson().toJson(it))
                .replace("\\u0000", "")
        }
        return data
    }

    private fun convertKeysToJavascriptFormat(slotJson: String): String {
        var convertedJson = slotJson.replace(
            com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_IBEACON_UUID,
            "KEY_ADVERTISING_CONTENT_IBEACON_UUID"
        )
        convertedJson = convertedJson.replace(
            com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_IBEACON_MAJOR,
            "KEY_ADVERTISING_CONTENT_IBEACON_MAJOR"
        )
        convertedJson = convertedJson.replace(
            com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_IBEACON_MINOR,
            "KEY_ADVERTISING_CONTENT_IBEACON_MINOR"
        )
        convertedJson = convertedJson.replace(
            com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_UID_NAMESPACE_ID,
            "KEY_ADVERTISING_CONTENT_UID_NAMESPACE_ID"
        )
        convertedJson = convertedJson.replace(
            com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_UID_INSTANCE_ID,
            "KEY_ADVERTISING_CONTENT_UID_INSTANCE_ID"
        )
        convertedJson = convertedJson.replace(
            com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_URL_URL,
            "KEY_ADVERTISING_CONTENT_URL_URL"
        )
        convertedJson = convertedJson.replace(
            com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_CUSTOM_CUSTOM,
            "KEY_ADVERTISING_CONTENT_CUSTOM_CUSTOM"
        )

        convertedJson = convertedJson.replace(
            com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_DEFAULT_DATA,
            "KEY_ADVERTISING_CONTENT_DEFAULT_DATA"
        )
        return convertedJson
    }

    inner class WebAppInterface {

        @JavascriptInterface
        fun onDataChanged(slotJsonRaw: String) {
            Timber.d("OnDataChanged: $slotJsonRaw")
            val slotJson = convertKeysToOriginals(slotJsonRaw)
            val slotJS = Gson().fromJson<SlotJS>(slotJson, SlotJS::class.java)
            val slotPosition = arguments!!.getInt(EXTRA_BEACON_SLOT_POSITION)
            val dataSlot : List<Slot>

            dataSlot = slots.filter {
                Timber.d("Tags: ${it.getType().tabName} ${slotJS.frameType}")
                it.getType().tabName == slotJS.frameType
            }.toHashSet().toList()

            dataSlot.forEach {
                Timber.d("DataSlot: ${it.name} ${slotJS.frameType}")
            }

            if(dataSlot.isNotEmpty()) {
                beaconViewModel.beacon.value?.slots?.set(slotPosition, dataSlot[0])
            }
        }

        private fun convertKeysToOriginals(slotJson: String): String {
            var convertedJson = slotJson.replace(
                "KEY_ADVERTISING_CONTENT_IBEACON_UUID",
                com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_IBEACON_UUID
            )
            convertedJson = convertedJson.replace(
                "KEY_ADVERTISING_CONTENT_IBEACON_MAJOR",
                com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_IBEACON_MAJOR
            )
            convertedJson = convertedJson.replace(
                "KEY_ADVERTISING_CONTENT_IBEACON_MINOR",
                com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_IBEACON_MINOR
            )
            convertedJson = convertedJson.replace(
                "KEY_ADVERTISING_CONTENT_UID_NAMESPACE_ID",
                com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_UID_NAMESPACE_ID
            )
            convertedJson = convertedJson.replace(
                "KEY_ADVERTISING_CONTENT_UID_INSTANCE_ID",
                com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_UID_INSTANCE_ID
            )
            convertedJson = convertedJson.replace(
                "KEY_ADVERTISING_CONTENT_URL_URL",
                com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_URL_URL
            )
            convertedJson = convertedJson.replace(
                "KEY_ADVERTISING_CONTENT_CUSTOM_CUSTOM",
                com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_CUSTOM_CUSTOM
            )
            convertedJson = convertedJson.replace(
                "KEY_ADVERTISING_CONTENT_DEFAULT_DATA",
                com.aconno.bluetooth.beacon.Slot.KEY_ADVERTISING_CONTENT_DEFAULT_DATA
            )
            return convertedJson
        }

        @JavascriptInterface
        fun stopViewPager() {
            activity?.let {
                it as ViewPagerSlider
            }?.let {
                it.stopViewPager()
            }
        }

        @JavascriptInterface
        fun startViewPager() {
            activity?.let {
                it as ViewPagerSlider
            }?.let {
                it.startViewPager()
            }
        }

        @JavascriptInterface
        fun onError(string: String) {
            Timber.e(string)
        }
    }

    override fun onDestroyView() {
        view?.let {
        }

        super.onDestroyView()
    }

    companion object {
        const val HTML_FILE_PATH =
            "file:///android_asset/resources/settings/views/slot/Slot.html"

        @JvmStatic
        fun newInstance(slotPosition: Int) =
            BeaconSettingsSlotFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_BEACON_SLOT_POSITION, slotPosition)
                }
            }
    }
}