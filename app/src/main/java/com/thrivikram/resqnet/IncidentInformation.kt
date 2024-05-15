package com.thrivikram.resqnet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class IncidentInformation : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1 // Request code for picking an image
    private lateinit var selectedImageView: ImageView
    private lateinit var reportButton: Button
    private lateinit var closeButton: ImageView
    private lateinit var imagePath: String
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private val PREFS_NAME = "com.resqnet.prefs"
    private val KEY_LATITUDE = "latitude"
    private val KEY_LONGITUDE = "longitude"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incident_information)

        val reportPhotoTextView: TextView = findViewById(R.id.report_photo)
        selectedImageView = findViewById(R.id.selectedImageView)
        closeButton = findViewById(R.id.close_icon)
        val radioGroup = findViewById<RadioGroup>(R.id.incident_location_group)
        val radioGroup2 = findViewById<RadioGroup>(R.id.emergency_type_radio_group)
        val manualLocation = findViewById<TextView>(R.id.report_manual_location)
        reportButton = findViewById(R.id.report_button)
        val descriptionEditText = findViewById<EditText>(R.id.report_description)

        val prefs = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        latitude = prefs.getFloat(KEY_LATITUDE, 0.0f).toDouble()
        longitude = prefs.getFloat(KEY_LONGITUDE, 0.0f).toDouble()

        imagePath = ""

        updateReportButtonState(false, false)

        closeButton.setOnClickListener {
            finish()
        }

        reportPhotoTextView.setOnClickListener {
            openFileChooser()
        }

        radioGroup2.setOnCheckedChangeListener { _, _ ->
            updateReportButtonState(
                descriptionEditText.text.toString().isNotEmpty(),
                isAnyRadioButtonSelected(radioGroup2)
            )
        }

        descriptionEditText.addTextChangedListener {
            updateReportButtonState(
                descriptionEditText.text.toString().isNotEmpty(),
                isAnyRadioButtonSelected(radioGroup) && isAnyRadioButtonSelected(radioGroup2)
            )
        }


        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            updateReportButtonState(
                descriptionEditText.text.toString().isNotEmpty(),
                isAnyRadioButtonSelected(radioGroup)
            )
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
            submitReport()
        }
    }

    private fun submitReport() {
        val randomId = generateRandomId()

        val selectedEmergencyOption = findViewById<RadioButton>(findViewById<RadioGroup>(R.id.emergency_type_radio_group).checkedRadioButtonId).text.toString()

        val selectedLocationOption = if (findViewById<RadioButton>(findViewById<RadioGroup>(R.id.incident_location_group).checkedRadioButtonId).text.toString().contains("location")
        ) {
            "${latitude}, ${longitude}"
        } else {
            findViewById<TextView>(R.id.report_manual_location).text.toString()
        }

        val shortDescription = findViewById<EditText>(R.id.report_description).text.toString()

        val prefs = this.getSharedPreferences("com.resqnet.prefs", Context.MODE_PRIVATE)
        val aadhaar = prefs.getString("Aadhaar", "")

        val reportStatus = 0

        val report = aadhaar?.let {
            ReportEntity(randomId, selectedEmergencyOption, selectedLocationOption, shortDescription, imagePath,
                it, reportStatus)
        }

        lifecycleScope.launch {
            if (report != null) {
                ResQNet.database.reportDao().insertReport(report)
            }
        }
        Toast.makeText(this, "Report Submitted.${aadhaar}", Toast.LENGTH_SHORT).show()

        finish()
    }

    private fun generateRandomId(): String {
        val allowedChars = ('A'..'Z') + ('0'..'9')
        return (1..8)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri = data.data!!
            selectedImageView.setImageURI(selectedImageUri)
            imagePath = getPathFromUri(selectedImageUri)
            selectedImageView.visibility = ImageView.VISIBLE
        }
    }

    private fun updateReportButtonState(isDescriptionFilled: Boolean, isRadioButtonSelected: Boolean) {
        val backgroundColor = if (isDescriptionFilled && isRadioButtonSelected) R.color.green else R.color.red
        val color = ContextCompat.getColor(this, backgroundColor)
        reportButton.isEnabled = isDescriptionFilled && isRadioButtonSelected
        reportButton.backgroundTintList = ColorStateList.valueOf(color)
    }

    private fun isAnyRadioButtonSelected(radioGroup: RadioGroup): Boolean {
        return radioGroup.checkedRadioButtonId != -1
    }

    private fun getPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            return it.getString(columnIndex)
        }
        return ""
    }
}
