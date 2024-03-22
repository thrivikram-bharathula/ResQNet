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

    lateinit var mobileNumberWarning : TextView
    lateinit var SignUpMobileNumber : EditText
    lateinit var continueBtn : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_registration)

        var MobileNumber : String = "123456789"
        continueBtn = findViewById(R.id.userRegistration_continue)
        continueBtn.isClickable = false
        SignUpMobileNumber = findViewById(R.id.userRegistration_MobileNumber)
        mobileNumberWarning = findViewById(R.id.userRegistration_mobileNumberWarning)
        SignUpMobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val Numbertext = s.toString()
                if(Numbertext.length<10)
                {
                    continueBtn.isClickable = false
                    continueBtn.setBackgroundColor(resources.getColor(R.color.grey))
                    mobileNumberWarning.text = "*Mobile Number should be atleast 10 digits"
                    mobileNumberWarning.setTextColor(
                        ContextCompat.getColor(this@UserRegistration,
                        R.color.red
                    ))
                }
                else
                {
                    continueBtn.isClickable = true
                    continueBtn.setBackgroundColor(resources.getColor(R.color.card_blue))
                    MobileNumber = s.toString()
                    mobileNumberWarning.text = "Mobile number is valid"
                    mobileNumberWarning.setTextColor(
                        ContextCompat.getColor(this@UserRegistration,
                        R.color.green
                    ))
                }
            }
        })

        continueBtn.setOnClickListener {

            val intent = Intent(this,MobileNumberOTP::class.java)
            intent.putExtra("MOBILENUMBER", MobileNumber)
            startActivity(intent)
        }
    }
}