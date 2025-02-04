package com.example.numad25sp_hongguo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the buttons
        Button aboutButton = findViewById(R.id.aboutButton);
        Button menuButton = findViewById(R.id.menuButton);

        // About button setup
        aboutButton.setBackgroundColor(android.graphics.Color.parseColor("#ADD8E6"));
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                    MainActivity.this,
                    "Name: Hong Guo\nEmail: guo.hong1@northeastern.edu",
                    Toast.LENGTH_LONG
                ).show();
            }
        });

        // Menu button setup
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.option1) {
                    Toast.makeText(MainActivity.this, "Option 1 clicked", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.option2) {
                    Toast.makeText(MainActivity.this, "Option 2 clicked", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        popup.show();
    }
} 