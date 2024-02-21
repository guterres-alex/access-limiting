package com.vicarius.accesslimitingtest.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {

    private String id;

    private String firstName;

    private String lastName;

    private LocalDateTime lastLoginTimeUtc;

}
