package com.thrivikram.resqnet

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.thrivikram.resqnet.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UserSOSFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var locationManager: LocationManager
    private lateinit var locationTextView: TextView
    private val viewOnMapsButton = view?.findViewById<Button>(R.id.userSOS_viewOnMapsButton)
    private var lastLatitude = 12.345
    private var lastLongitude = 67.345



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_s_o_s, container, false)

        val sosButton = view.findViewById<ImageButton>(R.id.userSOS_SOSButton)
        sosButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:112")
            startActivity(intent)
        }

        locationTextView = view.findViewById(R.id.userSOS_locationText)

        val updateLocationButton = view.findViewById<AppCompatImageButton>(R.id.userSOS_updateLocationButton)
        updateLocationButton.setOnClickListener {
            checkLocationPermission()
        }

        viewOnMapsButton?.setOnClickListener {
            val latitude = lastLatitude // replace with your latitude
            val longitude = lastLongitude // replace with your longitude
            val label = "My Location" // optional label for the marker

            val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($label)"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                // If Google Maps app is not installed, try opening in a browser
                val mapsUrl = "http://maps.google.com/maps?q=loc:$latitude,$longitude"
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl))
                startActivity(browserIntent)
            }
        }

        // Check location permission when the fragment is created
        checkLocationPermission()

        return view
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            updateLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationTextView.text = "Getting location..."
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationManager.removeUpdates(this)

            lastLatitude = location.latitude
            lastLongitude = location.longitude
            locationTextView.text = "Latitude: $lastLatitude, Longitude: $lastLongitude"
            locationTextView.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderDisabled(provider: String) {}
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocation()
            } else {
                // Handle permission denied
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserSOSFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
