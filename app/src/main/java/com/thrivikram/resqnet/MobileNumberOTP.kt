package com.thrivikram.resqnet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

class MobileNumberOTP : AppCompatActivity() {

    lateinit var MobileVerificationNumber : TextView
    lateinit var MobileVerificationScreenBack : ImageView
    lateinit var MobileNumberEdit : ImageView
    lateinit var MobileNumberContinueBtn : AppCompatButton
    private lateinit var otpFields: Array<EditText>
    lateinit var MobileOtpCountDown : TextView
    lateinit var MobileResendOtp : AppCompatButton

    var isOtpEntered = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_number_otp)

        MobileVerificationNumber = findViewById(R.id.mobileNumberVerification_number)
        MobileVerificationScreenBack = findViewById(R.id.mobileNumberVerification_back)
        MobileNumberEdit = findViewById(R.id.mobileNumberVerification_edit)
        MobileNumberContinueBtn = findViewById(R.id.mobileNumberVerification_confirm)

        MobileOtpCountDown = findViewById(R.id.mobileVerification_CountDown)
        MobileResendOtp = findViewById(R.id.mobileNumberVerification_resendOtp)

        val mobileToDisplay = intent.getStringExtra("MOBILENUMBER")
        MobileVerificationNumber.text = mobileToDisplay.toString()
        MobileResendOtp.isClickable = false

        object : CountDownTimer(30000,1000){
            override fun onTick(millisUntilFinished: Long) {
                Log.i("TIMETODO","${millisUntilFinished}")
                MobileOtpCountDown.text = "00:${millisUntilFinished/1000}"
                MobileResendOtp.isClickable = false
            }

            override fun onFinish() {
                findViewById<TextView>(R.id.ResendOtpTextView).visibility = TextView.INVISIBLE
                MobileOtpCountDown.visibility = TextView.INVISIBLE
                MobileResendOtp.isClickable = true
                MobileResendOtp.setBackgroundColor(resources.getColor(R.color.card_blue))
            }
        }.start()

        MobileResendOtp.setOnClickListener {

            Toast.makeText(this, "OTP Sent Successfully", Toast.LENGTH_SHORT).show()
            MobileResendOtp.visibility = AppCompatButton.INVISIBLE
        }

        otpFields = arrayOf(
            findViewById(R.id.mobileNumberVerification_otp1),
            findViewById(R.id.mobileNumberVerification_otp2),
            findViewById(R.id.mobileNumberVerification_otp3),
            findViewById(R.id.mobileNumberVerification_otp4),
            findViewById(R.id.mobileNumberVerification_otp5),
            findViewById(R.id.mobileNumberVerification_otp6)
        )
        setupOtpFields()


        val emailOtp = StringBuilder()
        for (i in 0 until otpFields.size) {

            emailOtp.append(otpFields[i].text.toString())
        }



//        if (emailOtp.length == 6) {
//            MobileNumberContinueBtn.isClickable = true
//            MobileNumberContinueBtn.setBackgroundColor(resources.getColor(R.color.card_blue))
//        }

        MobileVerificationScreenBack.setOnClickListener {
            finish()
        }

        MobileNumberContinueBtn.setOnClickListener {
            if(isOtpEntered) {

                val intent = Intent(this, RegistrationSuccessful::class.java)
                startActivity(intent)
            }
        }

        MobileNumberEdit.setOnClickListener {
            finish()
        }
    }

    private fun setupOtpFields() {
        for (i in 0 until otpFields.size) {
            otpFields[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    s?.let {
                        if(it.isEmpty()){
                            isOtpEntered = false
                            MobileNumberContinueBtn.setBackgroundColor(resources.getColor(R.color.grey))
                        }
                    }
                    if (s?.length == 1) {
                        moveFocusToNextField(i)
                    }
                    updateFieldBackground(i)
                }
            })

            otpFields[i].setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && otpFields[i].text.isNullOrEmpty()) {
                    moveFocusToPreviousField(i)

                    true
                } else {
                    false
                }
            }

        }
    }

    private fun moveFocusToNextField(currentIndex: Int) {
        if(currentIndex == otpFields.size -1){
            isOtpEntered = true
            MobileNumberContinueBtn.setBackgroundColor(resources.getColor(R.color.card_blue))
        }else{
            isOtpEntered = false
            MobileNumberContinueBtn.setBackgroundColor(resources.getColor(R.color.grey))
        }
        if (currentIndex < otpFields.size - 1) {
            otpFields[currentIndex + 1].requestFocus()
        }
    }

    private fun updateFieldBackground(index: Int) {
        val editText = otpFields[index]
        val digit = editText.text.toString().toIntOrNull()

        if (digit != null && digit in 0..9) {
            editText.setBackgroundResource(R.drawable.edit_text_background_blue)
        } else {
            editText.setBackgroundResource(R.drawable.edit_text_background)
        }
    }

    private fun moveFocusToPreviousField(currentIndex: Int) {
        isOtpEntered = false
        MobileNumberContinueBtn.setBackgroundColor(resources.getColor(R.color.grey))
        if (currentIndex > 0) {
            otpFields[currentIndex - 1].requestFocus()
        }
    }
}