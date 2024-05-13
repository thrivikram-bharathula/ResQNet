package com.thrivikram.resqnet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.airbnb.lottie.LottieAnimationView

class RegistrationSuccessful : AppCompatActivity() {

    private lateinit var animationView: LottieAnimationView
    lateinit var proceed : AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_successful)

        val prefs = getSharedPreferences("com.resqnet.prefs", Context.MODE_PRIVATE)

        prefs.edit().putBoolean("is_logged_out", false).apply()
        animationView = findViewById(R.id.animation_view)
        proceed = findViewById(R.id.SignUpCompatButton)

        animationView.setAnimation("animationCompleted.json")
        animationView.playAnimation()

        proceed.setOnClickListener {
            val intent = Intent(this, UserHomeScreen::class.java)
            startActivity(intent)
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Do nothing
    }
}