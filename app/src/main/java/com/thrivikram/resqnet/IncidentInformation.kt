package com.thrivikram.resqnet

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.health.connect.datatypes.units.Length
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import com.thrivikram.resqnet.UserSOSFragment

class IncidentInformation : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1 // Request code for picking an image
    private lateinit var selectedImageView: ImageView
    private lateinit var reportButton: Button
    private val PREFS_NAME = "com.resqnet.prefs"
    private val KEY_LATITUDE = "latitude"
    private val KEY_LONGITUDE = "longitude"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incident_information)

        val reportPhotoTextView: TextView = findViewById(R.id.report_photo)
        selectedImageView = findViewById(R.id.selectedImageView)
        val radioGroup = findViewById<RadioGroup>(R.id.incident_location_group)
        val radioGroup2 = findViewById<RadioGroup>(R.id.emergency_type_radio_group)
        val manualLocation = findViewById<TextView>(R.id.report_manual_location)
        reportButton = findViewById(R.id.report_button)
        val descriptionEditText = findViewById<EditText>(R.id.report_description)

        val prefs = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val latitude = prefs.getFloat(KEY_LATITUDE, 0.0f).toDouble()
        val longitude = prefs.getFloat(KEY_LONGITUDE, 0.0f).toDouble()

        reportPhotoTextView.setOnClickListener {
            openFileChooser()
        }

        radioGroup.setOnCheckedChangeListener { _, _ ->
            updateReportButtonState(descriptionEditText.text.toString().isNotEmpty(), radioGroup2.isSelected)
        }

        radioGroup2.setOnCheckedChangeListener { _, _ ->
            updateReportButtonState(descriptionEditText.text.toString().isNotEmpty(), radioGroup.isSelected)
        }

        descriptionEditText.addTextChangedListener {
            updateReportButtonState(descriptionEditText.text.toString().isNotEmpty(), manualLocation.text.toString().isNotEmpty())
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_your_location -> {
                    manualLocation.visibility = View.GONE
                    if (latitude != 0.0 || longitude != 0.0) {
                        Toast.makeText(this,"Your Location : \n${latitude}, \n${longitude}", Toast.LENGTH_SHORT).show()
                    } else {
                        manualLocation.visibility = View.VISIBLE
                        Toast.makeText(this, "Location not found. Please enter Manually.", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.radio_other -> {
                    manualLocation.visibility = View.VISIBLE
                }
            }
        }
        reportButton.setOnClickListener {
            Toast.makeText(this, "Report Submitted.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun openFileChooser() {
        // Create an Intent to pick an image from the device
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*" // Limit to images only
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false) // Allow only one image to be selected
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Handle the result of the image selection
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri = data.data!!
            // Load the selected image into the ImageView
            selectedImageView.setImageURI(selectedImageUri)
            selectedImageView.visibility = ImageView.VISIBLE // Show the ImageView
        }
    }
    // Function to update the state of the report button
    private fun updateReportButtonState(isDescriptionFilled: Boolean, isRadioButtonSelected: Boolean) {
        if (isDescriptionFilled && isRadioButtonSelected) {
            // Enable report button and change background color to green
            reportButton.apply {
                isEnabled = true
                setBackgroundColor(resources.getColor(R.color.green))
            }
        } else {
            // Disable report button and change background color to red
            reportButton.apply {
                isEnabled = false
                setBackgroundColor(resources.getColor(R.color.red))
            }
        }
    }
}
