package com.example.numad25sp_hongguo

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Set the layout first

        // Find the button after setting the content view
        val aboutButton: Button = findViewById(R.id.aboutButton)

        // Change the background color of About Me button
        aboutButton.setBackgroundColor(android.graphics.Color.parseColor("#ADD8E6"))

        // Set the click listener
        aboutButton.setOnClickListener {
            Toast.makeText(
                this,
                "Name: Hong Guo\nEmail: guo.hong1@northeastern.edu",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}