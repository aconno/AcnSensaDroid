package com.aconno.acnsensa.ui.beacons

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aconno.acnsensa.R
import com.aconno.acnsensa.adapter.BeaconAdapter
import com.aconno.acnsensa.adapter.ItemClickListener
import com.aconno.acnsensa.domain.model.Device
import com.aconno.acnsensa.ui.MainActivity
import com.aconno.acnsensa.viewmodel.DeviceListViewModel
import kotlinx.android.synthetic.main.fragment_device_list.*
import timber.log.Timber
import javax.inject.Inject

class DeviceListFragment : Fragment(), ItemClickListener<Device> {

    @Inject
    lateinit var deviceListViewModel: DeviceListViewModel

    private lateinit var beaconAdapter: BeaconAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainActivity: MainActivity? = activity as MainActivity
        mainActivity?.mainActivityComponent?.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_device_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_devices.layoutManager = LinearLayoutManager(context)
        beaconAdapter = BeaconAdapter(mutableListOf(), this)
        list_devices.adapter = beaconAdapter

        deviceListViewModel.getPreferredDevicesLiveData().observe(this, Observer {
            displayPreferredDevices(it)
        })

        button_add_device.setOnClickListener {
            Timber.d("Button add device clicked")
        }
    }

    private fun displayPreferredDevices(preferredDevices: List<Device>?) {
        preferredDevices?.let {
            if (preferredDevices.isEmpty()) {
                empty_view.visibility = View.VISIBLE
                beaconAdapter.clearBeacons()
            } else {
                empty_view.visibility = View.INVISIBLE
                beaconAdapter.setBeacons(preferredDevices)
            }
        }
    }

    override fun onItemClick(item: Device) {
        activity?.let {
            val mainActivity = it as MainActivity
            mainActivity.showSensorValues(item.macAddress)
        }
    }

    companion object {

        fun newInstance(): DeviceListFragment {
            return DeviceListFragment()
        }
    }
}