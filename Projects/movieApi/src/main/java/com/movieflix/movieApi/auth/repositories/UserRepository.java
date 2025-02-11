package com.movieflix.movieApi.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieflix.movieApi.auth.entities.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	// we don't have findByUserName so we are writing this
	
	public Optional<User>  findByEmail(String username);

}
