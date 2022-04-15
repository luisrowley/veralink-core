package com.veralink.data;

import org.springframework.data.repository.CrudRepository;

import com.veralink.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}
