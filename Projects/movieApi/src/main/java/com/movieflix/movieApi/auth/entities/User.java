package com.movieflix.movieApi.auth.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User implements UserDetails {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userID;
	
	
	@NotBlank(message = "The name field can't be blank!")
	private String name;
	
	
	@NotBlank(message = "The userName field can't be blank!")
	@Column(unique = true)
	private String userName;
	
	@NotBlank(message = "The email field can't be blank!")
	@Column(unique = true)
	@Email(message = "please enter email in proper format!!")
	private String email;
	
	@NotBlank(message = "The password field can't be blank!")
	@Size(min = 5, message = "The password must have atleast 5 characters!")
	private String password;
	
	@OneToOne(mappedBy = "user")
	private ForgotPassword forgotPassword;
	
	@OneToOne(mappedBy = "user")
	private RefreshToken refreshToken;
	
	@Enumerated(EnumType.STRING)
	private UserRole role;
	
	
	private boolean isEnabled = true;
	
	private boolean isAccountNonExpired = true;
	
	private boolean isAccountNonLocked = true;
	
	private boolean isCredentialsNonExpired = true;

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}