package com.thrivikram.resqnet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.airbnb.lottie.LottieAnimationView

class RegistrationSuccessful : AppCompatActivity() {

    private lateinit var animationView: LottieAnimationView
    lateinit var startTradingBtn : AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_successful)

        animationView = findViewById(R.id.animation_view)
        startTradingBtn = findViewById(R.id.SignUpCompatButton)

//        startTradingBtn.setOnClickListener {
//
//            val intent = Intent(this,MainActivity::class.java)
//            startActivity(intent)
//        }
        animationView = findViewById(R.id.animation_view)
        animationView.setAnimation("animationCompleted.json")
        animationView.playAnimation()

    }
}