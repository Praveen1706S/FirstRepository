package com.movieflix.movieApi.auth.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
	
	private String name;
	
	private String email;
	
	private String userName;
	
	private String password;
	
	

}
