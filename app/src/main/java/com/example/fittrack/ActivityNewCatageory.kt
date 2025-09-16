package com.example.fittrack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fittrack.databinding.ActivityNewCatageoryBinding

class ActivityNewCatageory : AppCompatActivity() {
    private lateinit var binding : ActivityNewCatageoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewCatageoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}