package com.vicarius.accesslimitingtest.controller;

import com.vicarius.accesslimitingtest.dto.request.UserRequestDto;
import com.vicarius.accesslimitingtest.dto.response.UserQuotaResponseDto;
import com.vicarius.accesslimitingtest.dto.response.UserResponseDto;
import com.vicarius.accesslimitingtest.exception.UserNotFoundException;
import com.vicarius.accesslimitingtest.mapper.UserMapper;
import com.vicarius.accesslimitingtest.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Optional.ofNullable;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return ofNullable(service.createUser(userRequestDto))
            .map(UserMapper.INSTANCE::userToUserResponseDto)
            .orElseThrow();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable String id) {
        return service.getUser(id)
            .map(UserMapper.INSTANCE::userToUserResponseDto)
            .orElseThrow(UserNotFoundException::new);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(
        @PathVariable String id, @RequestBody UserRequestDto updatedUser) {
        return ofNullable(service.updateUser(id, updatedUser))
            .map(UserMapper.INSTANCE::userToUserResponseDto)
            .orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        service.deleteUser(id);
    }

    @PostMapping("/{id}/quota")
    public Boolean consumeQuota(@PathVariable String id) {
        return service.allowRequest(id);
    }

    @GetMapping("/quotas")
    public List<UserQuotaResponseDto> getUsersQuota() {
        return service.getUsersQuota();
    }

}
