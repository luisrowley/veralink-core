package com.veralink.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.veralink.model.User;

@Component
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	  User findByName(String name);
}
