package com.aconno.sensorics.ui.settings_framework.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.aconno.sensorics.R
import kotlinx.android.synthetic.main.fragment_beacon_general.*

open class BeaconSettingsSlotFragment : SettingsBaseFragment() {

    private var slotPosition: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        slotPosition = arguments?.getInt(EXTRA_BEACON_SLOT_POSITION, -1)?.takeIf {
            it != -1
        } ?: throw IllegalStateException(
            "Started BeaconSlotFragment without EXTRA_BEACON_SLOT_POSITION argument!"
        )
        return inflater.inflate(R.layout.fragment_beacon_general, container, false)
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
        webview_general.addJavascriptInterface(UpdateBeaconJsInterfaceImpl(), "Android")
        webview_general.webViewClient = PageLoadedEventWebViewClient {
            settingsActivitySharedViewModel.beaconJsonLiveDataForFragments.observe(
                viewLifecycleOwner,
                Observer { beaconJson ->
                    beaconJson?.let {
                        webview_general.loadUrl(jsGenerator.generateCall("init", it, slotPosition))
                    }
                })
        }
        webview_general.loadUrl(HTML_FILE_PATH)
    }

    companion object {
        const val HTML_FILE_PATH =
            "file:///android_asset/resources/settings/views/slot/Slot.html"
        const val EXTRA_BEACON_SLOT_POSITION = "com.aconno.beaconapp.BEACON_SLOT_POSITION"

        @JvmStatic
        fun newInstance(slotPosition: Int) =
            BeaconSettingsSlotFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_BEACON_SLOT_POSITION, slotPosition)
                }
            }
    }
}