package com.vicarius.accesslimitingtest.service;

import com.vicarius.accesslimitingtest.dto.request.UserRequestDto;
import com.vicarius.accesslimitingtest.dto.response.UserQuotaResponseDto;
import com.vicarius.accesslimitingtest.exception.UserNotFoundException;
import com.vicarius.accesslimitingtest.model.User;
import com.vicarius.accesslimitingtest.repository.UserRepository;
import com.vicarius.accesslimitingtest.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    @Mock
    private QuotaLimiter quotaLimiter;

    private User user;

    private UserRequestDto userRequestDto;

    @BeforeEach
    void setup() {
        user = User.builder()
                .id("1")
                .firstName("A")
                .lastName("B")
                .lastLoginTimeUtc(LocalDateTime.now())
                .build();

        userRequestDto = UserRequestDto.builder()
                .id("1")
                .firstName("A")
                .lastName("B")
                .lastLoginTimeUtc(LocalDateTime.now())
                .build();
    }

    @Test
    void createUser() {

        when(repository.save(any())).thenReturn(user);

        User savedUser = service.createUser(userRequestDto);

        assertNotNull(savedUser);
        assertEquals(user, savedUser);
    }

    @Test
    void getUser() {
        String id = "1";

        when(repository.findById(id)).thenReturn(Optional.of(user));

        Optional<User> savedUser = service.getUser(id);

        assertTrue(savedUser.isPresent());
        assertEquals(user, savedUser.get());
    }

    @Test
    void updateUser() {
        String id = "1";

        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(repository.save(any())).thenReturn(user);

        User savedUser = service.updateUser(id, userRequestDto);

        assertNotNull(savedUser);
        assertEquals(user, savedUser);
    }

    @Test
    void updateUserException() {
        String id = "1";

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.updateUser(id, userRequestDto));
    }

    @Test
    void deleteUser() {
        String id = "1";

        when(repository.findById(id)).thenReturn(Optional.of(user));

        service.deleteUser(id);

        verify(repository, times(1)).delete(user);
    }

    @Test
    void deleteUserException() {
        String id = "1";

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.deleteUser(id));
    }

    @Test
    void allowRequest() {
        String id = "1";

        when(repository.findById(id)).thenReturn(Optional.of(user));
        when(quotaLimiter.allowRequest(id, 0, 0)).thenReturn(true);

        boolean response = service.allowRequest(id);

        assertTrue(response);
    }

    @Test
    void getUsersQuota() {

        User user2 = User.builder()
                .id("2")
                .firstName("C")
                .lastName("D")
                .lastLoginTimeUtc(LocalDateTime.now())
                .build();

        when(repository.findAll()).thenReturn(List.of(user, user2));
        when(quotaLimiter.getUsersQuota()).thenReturn(Map.of(user.getId(), 2, user2.getId(), 4));

        List<UserQuotaResponseDto> response = service.getUsersQuota();

        assertEquals(2, response.size());
        assertEquals(user.getFirstName(), response.get(0).getFirstName());
        assertEquals(2, response.get(0).getRemainingTokens());
        assertEquals(user2.getFirstName(), response.get(1).getFirstName());
        assertEquals(4, response.get(1).getRemainingTokens());
    }

}
