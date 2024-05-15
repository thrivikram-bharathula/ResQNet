package com.thrivikram.resqnet

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

class login_common : AppCompatActivity() {
    private  lateinit var heading : TextView
    private  lateinit var continueBtn : AppCompatButton
    private  lateinit var userName : EditText
    private  lateinit var passWord : EditText
    private lateinit var warning : TextView

    var usernameEntered : Boolean = false
    var passwordEntered : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_common)
    }

    override fun onResume() {
        super.onResume()
        heading = findViewById(R.id.Heading)
        userName = findViewById(R.id.login_userName)
        passWord = findViewById(R.id.login_password)
        continueBtn = findViewById(R.id.login_continue)
        warning = findViewById(R.id.login_warning)

        val headingToDisplay = intent.getStringExtra("Type")
        heading.text =  headingToDisplay + " Login"
        continueBtn.isClickable = false

        userName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.length > 0) {
                    usernameEntered = true
                    if (passwordEntered) {
                        warning.visibility = View.INVISIBLE
                        continueBtn.isClickable = true
                        continueBtn.setBackgroundColor(resources.getColor(R.color.card_blue))
                    } else {
                        warning.visibility = View.VISIBLE
                        continueBtn.isClickable = false
                        continueBtn.setBackgroundColor(resources.getColor(R.color.grey))
                    }
                } else {
                    usernameEntered = false
                }
            }
        })

        passWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.length > 0) {
                    passwordEntered = true
                    if (usernameEntered) {
                        warning.visibility = View.INVISIBLE
                        continueBtn.isClickable = true
                        continueBtn.setBackgroundColor(resources.getColor(R.color.card_blue))
                    } else {
                        warning.visibility = View.VISIBLE
                        continueBtn.isClickable = false
                        continueBtn.setBackgroundColor(resources.getColor(R.color.grey))
                    }
                } else {
                    passwordEntered = false
                }
            }
        })

        continueBtn.setOnClickListener {
            Toast.makeText(this, "Sorry! Firebase is Down. Try after Some Time!", Toast.LENGTH_SHORT).show()
            //val intent = Intent(this, RegistrationSuccessful::class.java)
            //startActivity(intent)
        }
    }
}