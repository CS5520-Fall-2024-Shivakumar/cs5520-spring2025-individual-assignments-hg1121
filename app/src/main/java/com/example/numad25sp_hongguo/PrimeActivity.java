package com.example.numad25sp_hongguo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PrimeActivity extends AppCompatActivity implements PrimeThread.PrimeCallback {
    private TextView previousPrimeText;
    private TextView currentSearchingText;
    private Button findPrimesButton;
    private Button terminateButton;
    private CheckBox pacifierSwitch;
    private PrimeThread primeThread;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    
    // Keys for saving state
    private static final String KEY_IS_SEARCHING = "is_searching";
    private static final String KEY_CURRENT_NUMBER = "current_number";
    private static final String KEY_LAST_PRIME = "last_prime";
    private static final String KEY_PACIFIER_STATE = "pacifier_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime);

        // Initialize views
        previousPrimeText = findViewById(R.id.previousPrimeValue);
        currentSearchingText = findViewById(R.id.currentSearchingValue);
        findPrimesButton = findViewById(R.id.find_primes);
        terminateButton = findViewById(R.id.button10);
        pacifierSwitch = findViewById(R.id.checkBox);

        // set click listener for buttons
        findPrimesButton.setOnClickListener(v -> startPrimeSearch());
        terminateButton.setOnClickListener(v -> stopPrimeSearch());

        // for rotation state restore
        if (savedInstanceState != null) {
            pacifierSwitch.setChecked(savedInstanceState.getBoolean(KEY_PACIFIER_STATE, false));
            
            // Restore the last prime and current number
            String lastPrime = savedInstanceState.getString(KEY_LAST_PRIME, "3");
            String currentNumber = savedInstanceState.getString(KEY_CURRENT_NUMBER, "3");
            previousPrimeText.setText(lastPrime);
            currentSearchingText.setText(currentNumber);

            // If search was running, restart it from the saved number
            if (savedInstanceState.getBoolean(KEY_IS_SEARCHING, false)) {
                startPrimeSearch(Integer.parseInt(currentNumber));
            }
        } else {
            // start from beginning
            previousPrimeText.setText("3");
            currentSearchingText.setText("3");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        // Save whether search is running
        outState.putBoolean(KEY_IS_SEARCHING, primeThread != null && primeThread.isAlive());
        
        // Save current number and last prime
        outState.putString(KEY_CURRENT_NUMBER, currentSearchingText.getText().toString());
        outState.putString(KEY_LAST_PRIME, previousPrimeText.getText().toString());
        
        // Save pacifier switch state
        outState.putBoolean(KEY_PACIFIER_STATE, pacifierSwitch.isChecked());
    }

    private void startPrimeSearch() {
        startPrimeSearch(3); // Start from beginning
    }

    // search process resume after rotation
    private void startPrimeSearch(int startNumber) {
        if (primeThread != null && primeThread.isAlive()) {
            return; // Don't start a new search if one is already running
        }

        // Create and start new thread with the given number
        primeThread = new PrimeThread(this, startNumber);
        primeThread.start();

        // Update button states
        findPrimesButton.setEnabled(false);
        terminateButton.setEnabled(true);
    }

    private void stopPrimeSearch() {
        if (primeThread != null) {
            primeThread.stopSearch();
            // Update button states
            findPrimesButton.setEnabled(true);
            terminateButton.setEnabled(false);
        }
    }

    // receive updates from the background thread
    @Override
    public void onPrimeFound(int prevPrime) {
        mainHandler.post(() -> previousPrimeText.setText(String.valueOf(prevPrime)));
    }

    @Override
    public void onNumberChecked(int currNum) {
        mainHandler.post(() -> currentSearchingText.setText(String.valueOf(currNum)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (primeThread != null) {
            primeThread.stopSearch();
        }
    }
}
