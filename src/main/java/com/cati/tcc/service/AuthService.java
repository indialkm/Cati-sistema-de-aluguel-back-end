package com.cati.tcc.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cati.tcc.config.security.UserSpringSecurity;

@Service
public class AuthService {
	public UUID getAuthenticatedUserId() {
      
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof UserSpringSecurity userDetails) {
            return userDetails.id(); 
        }
        
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
    }
	
	

}
