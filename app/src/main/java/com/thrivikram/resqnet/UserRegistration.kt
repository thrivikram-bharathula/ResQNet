package com.thrivikram.resqnet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat

class UserRegistration : AppCompatActivity() {

    lateinit var aadhaarNumberWarning : TextView
    lateinit var aadhaarNumber : EditText
    lateinit var continueBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)

        var AadhaarNumber : String = "012345678910"
        continueBtn = findViewById(R.id.userRegistration_continue)
        continueBtn.isClickable = false
        aadhaarNumber = findViewById(R.id.userRegistration_aadhaarNumber)
        aadhaarNumberWarning = findViewById(R.id.userRegistration_aadharNumberWarning)
        aadhaarNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val Numbertext = s.toString()
                if(Numbertext.length<12)
                {
                    continueBtn.isClickable = false
                    continueBtn.setBackgroundColor(resources.getColor(R.color.grey))
                    aadhaarNumberWarning.text = "*Aadhaar Number should be atleast 12 digits"
                    aadhaarNumberWarning.setTextColor(
                        ContextCompat.getColor(this@UserRegistration,
                        R.color.red
                    ))
                }
                else
                {
                    continueBtn.isClickable = true
                    continueBtn.setBackgroundColor(resources.getColor(R.color.card_blue))
                    AadhaarNumber = s.toString()
                    aadhaarNumberWarning.text = "Aadhaar number is valid"
                    aadhaarNumberWarning.setTextColor(
                        ContextCompat.getColor(this@UserRegistration,
                        R.color.green
                    ))
                }
            }
        })

        continueBtn.setOnClickListener {

            val intent = Intent(this,MobileNumberOTP::class.java)
            intent.putExtra("AadhaarNumber", AadhaarNumber)
            startActivity(intent)
        }
    }
}