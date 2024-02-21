package com.vicarius.accesslimitingtest.service.impl;

import com.vicarius.accesslimitingtest.dto.request.UserRequestDto;
import com.vicarius.accesslimitingtest.dto.response.UserQuotaResponseDto;
import com.vicarius.accesslimitingtest.exception.UserNotFoundException;
import com.vicarius.accesslimitingtest.mapper.UserMapper;
import com.vicarius.accesslimitingtest.model.User;
import com.vicarius.accesslimitingtest.repository.UserRepository;
import com.vicarius.accesslimitingtest.service.QuotaLimiter;
import com.vicarius.accesslimitingtest.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final QuotaLimiter quotaLimiter;

    @Value("${quota-limiter.request-limit}")
    private int requestLimit;

    @Value("${quota-limiter.time-window-minutes}")
    private int timeWindowMinutes;

    public UserServiceImpl(UserRepository repository, QuotaLimiter quotaLimiter) {
        this.repository = repository;
        this.quotaLimiter = quotaLimiter;
    }

    public User createUser(UserRequestDto userRequestDto) {
        User user = UserMapper.INSTANCE.userDtoToUser(userRequestDto);
        return repository.save(user);
    }

    public Optional<User> getUser(String userId) {
        return repository.findById(userId);
    }

    public User updateUser(String userId, UserRequestDto updatedUserRequestDto) {
        Optional<User> user = repository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        User updatedUser = UserMapper.INSTANCE.userDtoToUser(updatedUserRequestDto);
        updatedUser.setId(userId);

        return repository.save(updatedUser);
    }

    public void deleteUser(String userId) {
        Optional<User> user = repository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        repository.delete(user.get());
        quotaLimiter.deleteUser(userId);
    }

    @Override
    public boolean allowRequest(String userId) {
        Optional<User> user = getUser(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        return quotaLimiter.allowRequest(userId, requestLimit, timeWindowMinutes);
    }

    @Override
    public List<UserQuotaResponseDto> getUsersQuota() {

        Iterable<User> userIterable = repository.findAll();
        Map<String, Integer> usersQuota = quotaLimiter.getUsersQuota();

        List<UserQuotaResponseDto> responseList = new ArrayList<>();
        userIterable.forEach(user -> {
            UserQuotaResponseDto response = UserQuotaResponseDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .lastLoginTimeUtc(user.getLastLoginTimeUtc())
                    .remainingTokens(requestLimit)
                    .build();
            if (usersQuota.containsKey(user.getId())) {
                response.setRemainingTokens(usersQuota.get(user.getId()));
            }
            responseList.add(response);
        });

        return responseList;
    }

}
