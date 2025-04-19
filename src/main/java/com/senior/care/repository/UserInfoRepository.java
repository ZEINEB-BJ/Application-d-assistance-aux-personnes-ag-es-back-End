package com.senior.care.repository;

import java.util.Optional;

import com.senior.care.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<User, Integer>  {
	
	 Optional<User> findByName(String username);

}
