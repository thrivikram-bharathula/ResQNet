package com.thrivikram.resqnet

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserHomeScreen : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var fragmentWrapper: FrameLayout

    private val fragment1 = UserSOSFragment()
    private val fragment2 = UserLiveFragment()
    private val fragment3 = UserProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_home_screen)

        bottomNavigationView = findViewById(R.id.bottom_nav)
        fragmentWrapper = findViewById(R.id.fragment_wrapper)

        // Set the default fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_wrapper, fragment1).commit()

        // Ensure bottomNavigationView is not null before casting
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_sos -> fragment1
                R.id.nav_live -> fragment2
                R.id.nav_profile -> fragment3
                else -> fragment1 // Set a default fragment in case of unexpected item ID
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_wrapper, selectedFragment).commit()
            true
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
    }
}
