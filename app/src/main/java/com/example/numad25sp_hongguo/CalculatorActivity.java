package com.example.numad25sp_hongguo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CalculatorActivity extends AppCompatActivity {
    private TextView displayText;
    private StringBuilder currentExpression = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        displayText = findViewById(R.id.calcDisplay);

        // Set up click listeners for all number buttons
        setupNumberButton(R.id.button0, "0");
        setupNumberButton(R.id.button1, "1");
        setupNumberButton(R.id.button2, "2");
        setupNumberButton(R.id.button3, "3");
        setupNumberButton(R.id.button4, "4");
        setupNumberButton(R.id.button5, "5");
        setupNumberButton(R.id.button6, "6");
        setupNumberButton(R.id.button7, "7");
        setupNumberButton(R.id.button8, "8");
        setupNumberButton(R.id.button9, "9");
        setupNumberButton(R.id.buttonPlus, "+");
        setupNumberButton(R.id.buttonMinus, "-");

        // Setup X button (delete)
        findViewById(R.id.buttonX).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentExpression.length() > 0) {
                    currentExpression.setLength(currentExpression.length() - 1);
                    updateDisplay();
                }
            }
        });

        // Setup equals button
        findViewById(R.id.buttonEquals).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });
    }

    private void setupNumberButton(int buttonId, final String value) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentExpression.append(value);
                updateDisplay();
            }
        });
    }

    private void updateDisplay() {
        if (currentExpression.length() == 0) {
            displayText.setText("CALC");
        } else {
            displayText.setText(currentExpression.toString());
        }
    }

    private void calculateResult() {
        try {
            String expression = currentExpression.toString();
            String[] parts;
            int result;

            if (expression.contains("+")) {
                parts = expression.split("\\+");
                result = Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]);
            } else if (expression.contains("-")) {
                parts = expression.split("-");
                result = Integer.parseInt(parts[0]) - Integer.parseInt(parts[1]);
            } else {
                return;
            }

            currentExpression.setLength(0);
            currentExpression.append(result);
            updateDisplay();
        } catch (Exception e) {
            displayText.setText("Error");
            currentExpression.setLength(0);
        }
    }
} 