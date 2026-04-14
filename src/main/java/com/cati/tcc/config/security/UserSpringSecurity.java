package com.cati.tcc.config.security;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cati.tcc.model.User;
import com.cati.tcc.model.enums.Role;

public record UserSpringSecurity(String username, String password, UUID id, Set<Role> roles) implements UserDetails {

	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    return roles.stream()
	            .map(role -> new SimpleGrantedAuthority(role.name())) 
	            .toList();
	}

	@Override
	public @Nullable String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public boolean hasRole(Role roles) {
		return getAuthorities().contains(new SimpleGrantedAuthority(roles.name()));
	}

	// Verifica lógica para login , exemplo: erro a senha mais de 3 vezes e etc
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	// Ciar lógica para verificar se o usuário stá ativo no banco de dados
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	
	@Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
	
}
