package com.movieflix.movieApi.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movieflix.movieApi.auth.entities.RefreshToken;

public interface RefreshTokenRepository  extends JpaRepository<RefreshToken, Integer>{
	
	// we don't have findByUserName so we are writing this
	
		public Optional<RefreshToken>  findByRefreshToken(String RefreshToken);


}
