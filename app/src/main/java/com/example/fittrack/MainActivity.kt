package com.example.fittrack

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fittrack.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var Binding: ActivityMainBinding
    private lateinit var adapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Agar first time nahi hai to yahan se nikal do
        if (!PrefManager.isFirstTimeLaunch(this)) {
            startActivity(Intent(this, ActivitySplashScreen::class.java))
            finish()
            return
        }
        enableEdgeToEdge()
        Binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(Binding.root)

        val items = listOf(
            Onboardingitem(R.drawable.ic_launcher_foreground, "Welcome", "Track your fitness easily"),
            Onboardingitem(R.drawable.ic_launcher_foreground, "Features", "Check steps, calories and more"),
            Onboardingitem(R.drawable.ic_launcher_foreground, "Get Started", "Let’s start your journey")
        )

        adapter = OnboardingAdapter(items)
        Binding.viewPager.adapter = adapter

        // Next button
        Binding.btnnext.setOnClickListener {
            if (Binding.viewPager.currentItem + 1 < items.size) {
                Binding.viewPager.currentItem += 1
            } else {
                goToNext()
            }
        }
        // Skip button
        Binding.btnskip.setOnClickListener {
            goToNext()
        }
    }

    private fun goToNext() {
        // Ab first time nahi hai
        PrefManager.setFirstTimeLaunch(this@MainActivity, false)
        startActivity(Intent(this@MainActivity, ActivitySplashScreen::class.java))
        finish()
    }
}

