package com.example.numad25sp_hongguo;

public class PrimeThread extends Thread {
    private volatile boolean isRunning = false;
    private final PrimeCallback callback;
    private int currentNumber;
    private int lastPrime;

    public interface PrimeCallback {
        void onPrimeFound(int prime);
        void onNumberChecked(int number);
    }

    public PrimeThread(PrimeCallback callback, int startNumber) {
        this.callback = callback;
        this.currentNumber = startNumber;
        this.lastPrime = 3; // Default last prime
    }

    @Override
    public void run() {
        isRunning = true;

        while (isRunning) {
            // Check if thread should stop
            if (!isRunning) {
                break;
            }

            if (isPrime(currentNumber)) {
                lastPrime = currentNumber;
                // Update UI with the new prime number
                if (callback != null && isRunning) {
                    callback.onPrimeFound(currentNumber);
                }
            }

            // Update UI with current number being checked
            if (callback != null && isRunning) {
                callback.onNumberChecked(currentNumber);
            }

            // Check again before incrementing
            if (!isRunning) {
                break;
            }

            currentNumber += 2;

            // perform like sleep to avoid updating too fast
            for (int i = 0; i < 1000000 && isRunning; i++) {
                // do nothing 
            }
        }
    }

    private boolean isPrime(int number) {
        for (long i = 3; i <= (int)Math.sqrt(number) + 1; i += 2) {
            // Check if thread should stop during long calculations
            if (!isRunning) {
                return false;
            }
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public void stopSearch() {
        isRunning = false;
    }

}
