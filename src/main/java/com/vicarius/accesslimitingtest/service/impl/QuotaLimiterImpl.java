package com.vicarius.accesslimitingtest.service.impl;

import com.vicarius.accesslimitingtest.model.TokenBucket;
import com.vicarius.accesslimitingtest.service.QuotaLimiter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class QuotaLimiterImpl implements QuotaLimiter {

    private final Map<String, TokenBucket> userQuotas;

    public QuotaLimiterImpl() {
        this.userQuotas = new HashMap<>();
    }

    public boolean allowRequest(String userId, int requests, int timeWindowInMinutes) {
        userQuotas.computeIfAbsent(userId, k -> new TokenBucket(requests, timeWindowInMinutes));
        TokenBucket tokenBucket = userQuotas.get(userId);
        return tokenBucket.request(1);
    }

    public synchronized void deleteUser(String userId) {
        userQuotas.remove(userId);
    }

    public Map<String, Integer> getUsersQuota() {
        Map<String, Integer> tokensUsedMap = new HashMap<>();
        for (Map.Entry<String, TokenBucket> entry : userQuotas.entrySet()) {
            tokensUsedMap.put(entry.getKey(), entry.getValue().getTokens());
        }
        return tokensUsedMap;
    }
}
