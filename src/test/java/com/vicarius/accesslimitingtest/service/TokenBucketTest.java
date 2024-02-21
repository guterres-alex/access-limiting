package com.vicarius.accesslimitingtest.service;

import com.vicarius.accesslimitingtest.model.TokenBucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TokenBucketTest {

    private TokenBucket tokenBucket;

    @BeforeEach
    void setup() {
        tokenBucket = new TokenBucket(5, 5);
    }

    @Test
    void requestTokenBucket() {

        boolean response = tokenBucket.request(2);

        assertTrue(response);
        assertEquals(3, tokenBucket.getTokens());

        response = tokenBucket.request(2);

        assertTrue(response);
        assertEquals(1, tokenBucket.getTokens());

        tokenBucket.setLastRefillTime(System.currentTimeMillis() - 1000000000);

        response = tokenBucket.request(1);

        assertTrue(response);
        assertEquals(4, tokenBucket.getTokens());

    }

}
