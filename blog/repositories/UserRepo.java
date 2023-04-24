package com.shyam.blog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

import com.shyam.blog.entity.User;

public interface UserRepo extends JpaRepository<User,Integer> {
 
	Optional<User> findByEmail(String email);
}
