package com.vicarius.accesslimitingtest.repository;

import com.vicarius.accesslimitingtest.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ElasticUserRepository {

    public User save(User entity) {
        System.out.println("Saving user");
        entity.setId("1");
        return entity;
    }

    public Optional<User> findById(String id) {
        System.out.println("Finding user");
        return Optional.of(User.builder()
                .id(id)
                .firstName("A")
                .lastName("B")
                .lastLoginTimeUtc(LocalDateTime.now())
                .build());
    }

    public void delete(User entity) {
        System.out.println("Deleting user");
    }

    public Iterable<User> findAll() {
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .id("1")
                .firstName("A")
                .lastName("B")
                .lastLoginTimeUtc(LocalDateTime.now())
                .build());
        return users;
    }
}
