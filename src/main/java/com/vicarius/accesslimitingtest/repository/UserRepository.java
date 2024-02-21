package com.vicarius.accesslimitingtest.repository;

import com.vicarius.accesslimitingtest.model.User;

import java.util.Optional;

public interface UserRepository {

    User save(User entity);

    Optional<User> findById(String id);

    void delete(User entity);

    Iterable<User> findAll();
}
