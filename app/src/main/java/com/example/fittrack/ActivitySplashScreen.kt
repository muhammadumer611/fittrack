package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fittrack.databinding.ActivitySplashScreenBinding

class ActivitySplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // SharedPreferences se name get karo
        val sharedPref = getSharedPreferences("MyPreference", MODE_PRIVATE)
        val name: String? = sharedPref.getString("username", null)

        // 3 second splash delay
        Handler(Looper.getMainLooper()).postDelayed({

            if (name.isNullOrEmpty()) {
                // Pehli dafa (user ka naam save nahi hai) → ActivityInfo pe bhejo
                startActivity(Intent(this@ActivitySplashScreen, ActivityInfo::class.java))
            }
            else {
                // Naam already saved hai → direct Dashboard
                startActivity(Intent(this@ActivitySplashScreen, ActivityDashBoard::class.java))
            }

            finish()

        }, 3000) // 3 sec delay
    }
}
