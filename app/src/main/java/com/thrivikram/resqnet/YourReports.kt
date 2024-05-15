package com.thrivikram.resqnet

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class YourReports : AppCompatActivity() {
    private lateinit var cardContainer: LinearLayout
    private lateinit var noReports: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_reports)

        cardContainer = findViewById(R.id.card_container)
        noReports = findViewById(R.id.nolive)
        val backButton = findViewById<ImageView>(R.id.back_icon)

        backButton.setOnClickListener {
            finish()
        }
        noReports.visibility = View.VISIBLE

        val prefs = this.getSharedPreferences("com.resqnet.prefs", Context.MODE_PRIVATE)
        val userAadhaar = prefs.getString("Aadhaar", "123456")

        // Fetch reports from the database for the given Aadhaar number
        if (userAadhaar !=null && userAadhaar!="") {
            GlobalScope.launch(Dispatchers.Main) {
                val reports = fetchReportsForUser(userAadhaar)
                // Inflate card layout for each report and add it to the card container
                if (reports.isEmpty()) {
                    noReports.visibility = View.VISIBLE
                } else {
                    noReports.visibility = View.GONE
                    reports.forEach { report ->
                        val cardView = layoutInflater.inflate(R.layout.reported_incident_card, cardContainer, false) as androidx.cardview.widget.CardView

                        populateCardWithData(cardView, report)
                        cardContainer.addView(cardView)
                    }
                }
            }
        }
    }

    private suspend fun fetchReportsForUser(aadhaar: String): List<ReportEntity> {
        // Fetch reports from the database where aadhaar column matches the user's Aadhaar number
        return ResQNet.database.reportDao().getReportsByAadhaar(aadhaar)
    }

    private fun populateCardWithData(cardView: androidx.cardview.widget.CardView, report: ReportEntity) {
        // Populate the card with report data
        cardView.findViewById<TextView>(R.id.incident_type).text = report.type
        cardView.findViewById<TextView>(R.id.report_status).text = getStatusText(report.status)
        cardView.findViewById<TextView>(R.id.incident_id).text = report.id
        cardView.findViewById<TextView>(R.id.incident_location).text = report.location

        // Delete button click listener
        cardView.findViewById<ImageButton>(R.id.report_delete).setOnClickListener {
            showDeleteConfirmationDialog(report, cardView)
        }

        // Call button click listener
        cardView.findViewById<ImageButton>(R.id.report_call).setOnClickListener {
            // Directly call emergency number (112 in this case)
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:112")
            startActivity(intent)
        }
    }

    private fun showDeleteConfirmationDialog(report: ReportEntity, cardView: CardView) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Are you sure you want to delete this report?")
        alertDialogBuilder.setPositiveButton("Yes") { dialog, _ ->
            // Delete report from database
            GlobalScope.launch(Dispatchers.Main) {
                deleteReportFromDatabase(report)
            }
            // Remove card from container
            cardContainer.removeView(cardView)
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private suspend fun deleteReportFromDatabase(report: ReportEntity) {
        // Delete report from the database
        ResQNet.database.reportDao().deleteReport(report)
    }


    private fun getStatusText(status: Int): String {
        // Define your status texts based on status codes
        return when (status) {
            0 -> "Pending"
            1 -> "Under Review"
            2 -> "Resolved"
            else -> "Unknown"
        }
    }
}