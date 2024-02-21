package com.vicarius.accesslimitingtest.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class UserQuotaResponseDto {

    private String id;

    private String firstName;

    private String lastName;

    private LocalDateTime lastLoginTimeUtc;

    private int remainingTokens;

}
