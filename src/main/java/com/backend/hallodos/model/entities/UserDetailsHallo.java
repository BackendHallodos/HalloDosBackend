package com.backend.hallodos.model.entities;


import java.util.Arrays;
import java.util.Collection;



import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



public class UserDetailsHallo implements UserDetails {
	
	Mahasiswa mahasiswa;

	public UserDetailsHallo(Mahasiswa mahasiswa) {
		this.mahasiswa = mahasiswa;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(mahasiswa.getRole());
		return Arrays.asList(authority);
	}

	@Override
	public String getPassword() {

		return mahasiswa.getPassword();
	}

	@Override
	public String getUsername() {
	
		return mahasiswa.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
	
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
	
		return true;
	}
	}



