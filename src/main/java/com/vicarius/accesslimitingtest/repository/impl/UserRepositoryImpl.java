package com.vicarius.accesslimitingtest.repository.impl;

import com.vicarius.accesslimitingtest.model.User;
import com.vicarius.accesslimitingtest.repository.ElasticUserRepository;
import com.vicarius.accesslimitingtest.repository.MySqlUserRepository;
import com.vicarius.accesslimitingtest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final MySqlUserRepository mySqlUserRepository;

    private final ElasticUserRepository elasticUserRepository;

    @Value("${repository.day-time.start.hour}")
    private int dayTimeHourStart;

    @Value("${repository.day-time.start.minute}")
    private int dayTimeMinuteStart;

    @Value("${repository.day-time.end.hour}")
    private int dayTimeHourEnd;

    @Value("${repository.day-time.end.minute}")
    private int dayTimeMinuteEnd;

    public UserRepositoryImpl(
        MySqlUserRepository mySqlUserRepository,
        ElasticUserRepository elasticUserRepository) {

        this.mySqlUserRepository = mySqlUserRepository;
        this.elasticUserRepository = elasticUserRepository;
    }

    public User save(User entity) {
        if (isDayTime()) {
            return mySqlUserRepository.save(entity);
        }
        return elasticUserRepository.save(entity);
    }

    public Optional<User> findById(String id) {
        if (isDayTime()) {
            return mySqlUserRepository.findById(id);
        }
        return elasticUserRepository.findById(id);
    }

    public void delete(User entity) {
        if (isDayTime()) {
            mySqlUserRepository.delete(entity);
        }
        elasticUserRepository.delete(entity);
    }

    @Override
    public Iterable<User> findAll() {
        if (isDayTime()) {
            return mySqlUserRepository.findAll();
        }
        return elasticUserRepository.findAll();
    }

    private boolean isDayTime() {
        return LocalTime.now().isAfter(LocalTime.of(dayTimeHourStart, dayTimeMinuteStart)) &&
                LocalTime.now().isBefore(LocalTime.of(dayTimeHourEnd, dayTimeMinuteEnd));
    }

}
