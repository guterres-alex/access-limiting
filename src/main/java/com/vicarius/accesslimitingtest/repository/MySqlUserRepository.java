package com.vicarius.accesslimitingtest.repository;

import com.vicarius.accesslimitingtest.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MySqlUserRepository extends CrudRepository<User, String> {

}
