package com.thrivikram.resqnet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {
    private lateinit var userButton : AppCompatButton
    private lateinit var volunteerButton : AppCompatButton
    private lateinit var publicAmenityButton : AppCompatButton
    private lateinit var reliefOrgButton : AppCompatButton
    private lateinit var helpLineButton : AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var intent : Intent
        userButton = findViewById(R.id.userButton)
        userButton.setOnClickListener{
            intent = Intent(this, login_common::class.java)
            startActivity(intent)
        }
        volunteerButton.setOnClickListener{
            intent = Intent(this, login_common::class.java)
            intent.putExtra("Type", "Volunteer")
            startActivity(intent)
        }
        publicAmenityButton.setOnClickListener{
            intent = Intent(this, login_common::class.java)
            intent.putExtra("Type", "Public Amenity")
            startActivity(intent)
        }
        reliefOrgButton.setOnClickListener{
            intent = Intent(this, login_common::class.java)
            intent.putExtra("Type", "Relief Organization")
            startActivity(intent)
        }
        helpLineButton.setOnClickListener{
            intent = Intent(this, login_common::class.java)
            intent.putExtra("Type", "Help Line")
            startActivity(intent)
        }
    }
}