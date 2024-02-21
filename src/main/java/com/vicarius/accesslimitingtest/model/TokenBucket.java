package com.vicarius.accesslimitingtest.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenBucket {
    private int capacity;
    private int tokens;
    private double tokensPerSecond;
    private long lastRefillTime;
    private int timeWindowInMinutes;

    public TokenBucket(int requests, int timeWindowInMinutes) {
        this.capacity = requests;
        this.tokens = capacity;
        this.tokensPerSecond = (double) requests / (60.0 * timeWindowInMinutes);
        this.lastRefillTime = System.currentTimeMillis();
        this.timeWindowInMinutes = timeWindowInMinutes;
    }

    private void refill() {
        long now = System.currentTimeMillis();
        double elapsedSeconds = (double) (now - lastRefillTime) / 1000.0;
        double tokensToAdd = elapsedSeconds * tokensPerSecond;
        tokens = Math.min(capacity, (int) (tokens + tokensToAdd));
        lastRefillTime = now;
    }

    public synchronized boolean request(int tokensRequested) {
        refill();

        if (tokens >= tokensRequested) {
            tokens -= tokensRequested;
            return true;
        } else {
            return false;
        }
    }

}
