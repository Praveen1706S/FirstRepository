package com.movieflix.movieApi.auth.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

	private String  email;
	
	private String password;
	
	
}
