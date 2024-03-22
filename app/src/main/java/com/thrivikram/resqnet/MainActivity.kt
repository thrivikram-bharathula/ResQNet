package com.thrivikram.resqnet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {
    lateinit var userButton : AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userButton = findViewById(R.id.userButton)
        userButton.setOnClickListener{
            val intent = Intent(this,UserRegistration::class.java)
            startActivity(intent)
        }
    }
}