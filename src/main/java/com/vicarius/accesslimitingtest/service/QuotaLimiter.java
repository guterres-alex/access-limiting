package com.vicarius.accesslimitingtest.service;

import java.util.Map;

public interface QuotaLimiter {

    boolean allowRequest(String userId, int requests, int timeWindowInMinutes);

    void deleteUser(String userId);

    Map<String, Integer> getUsersQuota();
}
