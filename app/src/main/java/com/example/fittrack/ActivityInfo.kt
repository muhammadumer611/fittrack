package com.example.fittrack

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.fittrack.databinding.ActivityInfoBinding

class ActivityInfo : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences (same key as splash screen)
        sharedPreferences = getSharedPreferences("MyPreference", MODE_PRIVATE)


        binding.btnnext.setOnClickListener {
            val name = binding.name.text.toString().trim()

            if (name.isEmpty()) {
                // Agar name empty hai
                binding.name.error = "Please enter your name"
                Toast.makeText(this@ActivityInfo, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else {
                // Name save karo
                sharedPreferences.edit().putString("username", name).apply()

                // Dashboard pe bhejo
                val intent = Intent(this@ActivityInfo, ActivityDashBoard::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
