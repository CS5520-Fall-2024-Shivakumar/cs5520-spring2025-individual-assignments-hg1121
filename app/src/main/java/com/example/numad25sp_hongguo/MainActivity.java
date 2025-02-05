package com.example.numad25sp_hongguo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the button after setting the content view
        Button aboutButton = findViewById(R.id.aboutButton);

        // Change the background color of About Me button
        aboutButton.setBackgroundColor(android.graphics.Color.parseColor("#ADD8E6"));

        // Set the click listener
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                    MainActivity.this,
                    getString(R.string.about_me_text),
                    Toast.LENGTH_LONG
                ).show();
            }
        });

        // Calculator button setup
        Button calcButton = findViewById(R.id.calcButton);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
                startActivity(intent);
            }
        });
    }
} 