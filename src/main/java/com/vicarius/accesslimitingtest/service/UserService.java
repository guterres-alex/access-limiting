package com.vicarius.accesslimitingtest.service;

import com.vicarius.accesslimitingtest.dto.request.UserRequestDto;
import com.vicarius.accesslimitingtest.dto.response.UserQuotaResponseDto;
import com.vicarius.accesslimitingtest.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(UserRequestDto userRequestDto);

    Optional<User> getUser(String userId);

    User updateUser(String userId, UserRequestDto updatedUser);

    void deleteUser(String userId);

    boolean allowRequest(String userId);

    List<UserQuotaResponseDto> getUsersQuota();
}
