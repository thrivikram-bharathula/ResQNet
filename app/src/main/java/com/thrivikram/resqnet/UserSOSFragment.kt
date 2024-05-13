package com.thrivikram.resqnet

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient

private const val LOCATION_PERMISSION_REQUEST_CODE = 123
private const val REQUEST_TURN_ON_LOCATION = 456
private const val LOCATION_UPDATE_TIMEOUT = 10000 // 10 seconds in milliseconds

class UserSOSFragment : Fragment(), LocationResultCallback {

    private lateinit var locationTextView: TextView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var settingsClient: SettingsClient
    private val PREFS_NAME = "com.resqnet.prefs"
    private val KEY_LATITUDE = "latitude"
    private val KEY_LONGITUDE = "longitude"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_s_o_s, container, false)

        // SOS Button functionality remains the same
        val sosButton = view.findViewById<ImageButton>(R.id.userSOS_SOSButton)
        sosButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:112")
            startActivity(intent)
        }

        locationTextView = view.findViewById(R.id.userSOS_locationText)

        val updateLocationButton = view.findViewById<AppCompatImageButton>(R.id.userSOS_updateLocationButton)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        settingsClient = LocationServices.getSettingsClient(requireContext())

        val reportIncident = view.findViewById<TextView>(R.id.userSOS_registerIncidentButton)
        reportIncident.setOnClickListener {
            val intent = Intent(requireContext(), IncidentInformation::class.java)
            startActivity(intent)
        }

        updateLocationButton.setOnClickListener {
            // Inside the fragment
            checkLocationPermission(this)

        }

        // View on Maps functionality remains the same
        val viewOnMapsButton = view?.findViewById<Button>(R.id.userSOS_viewOnMapsButton)
        viewOnMapsButton?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val label = "Your Location"

                        val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($label)"

                        // Check if Google Maps app is the default handler
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        val resolveInfo = requireActivity().packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
                        if (resolveInfo != null && resolveInfo.activityInfo.packageName == "com.google.android.apps.maps") {
                            // Google Maps is the default, launch directly
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            return@addOnSuccessListener // Exit the listener after launching Maps
                        }

                        // Fallback to browser if not default
                        val mapsUrl = "http://maps.google.com/maps?q=loc:$latitude,$longitude"
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsUrl))
                        startActivity(browserIntent)
                    } else {
                        Toast.makeText(requireContext(), "Location not available. Please update location.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Request location permission if not granted
                checkLocationPermission(this)

            }
        }
        checkLocationPermission(object : LocationResultCallback {
            override fun onLocationResult(location: Location?) {
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    locationTextView.text = "Current Location : $latitude $longitude"
                } else {
                    Toast.makeText(requireContext(), "Location not found.", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return view
    }

    fun checkLocationPermission(callback: LocationResultCallback) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            checkLocationSettings(callback)
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkLocationSettings(callback: LocationResultCallback) {
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                // Location settings are satisfied, try to get location
                Toast.makeText(requireContext(), "Updating Location..", Toast.LENGTH_SHORT).show()
                updateLocation(callback)
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    val statusCode = exception.statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            // Show the user a dialog requesting to enable location
                            try {
                                exception.startResolutionForResult(
                                    requireActivity(),
                                    REQUEST_TURN_ON_LOCATION
                                )
                            } catch (e: Exception) {
                                // Handle error
                                Toast.makeText(requireContext(), "location unavailable", Toast.LENGTH_SHORT).show()
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            // Location settings are unavailable, handle error
                            Toast.makeText(requireContext(), "location settings disabled", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }
    @SuppressLint("MissingPermission")
    private fun updateLocation(callback: LocationResultCallback) {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                callback.onLocationResult(location)
                // Remove location updates after receiving the result
                fusedLocationProviderClient.removeLocationUpdates(this)
            }
        }

        val locationRequest = LocationRequest.create()
            .setPriority(PRIORITY_BALANCED_POWER_ACCURACY)
            .setInterval(5000) // Request location updates every 5 seconds (optional)

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun onLocationResult(location: Location?) {
        if (location != null) {
            // Location is available, use it
            val latitude = location.latitude
            val longitude = location.longitude
            val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putFloat(KEY_LATITUDE, latitude.toFloat())
            editor.putFloat(KEY_LONGITUDE, longitude.toFloat())
            editor.apply()
            locationTextView.text = "Current Location : $latitude $longitude"
            Toast.makeText(requireContext(), "Location updated.", Toast.LENGTH_SHORT).show()
        } else {
            // Location is not available
            Toast.makeText(requireContext(), "Location not found.", Toast.LENGTH_SHORT).show()
        }
    }
}

