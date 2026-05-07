package com.cati.tcc.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cati.tcc.config.security.UserSpringSecurity;
import com.cati.tcc.dto.request.DadosAutenticacao;
import com.cati.tcc.dto.response.UserLoginResponse;
import com.cati.tcc.dto.response.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    public UserLoginResponse autenticar(DadosAutenticacao dados) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            dados.username(), 
            dados.password()
        );

        Authentication authentication = manager.authenticate(authenticationToken);
        UserSpringSecurity userPrincipal = (UserSpringSecurity) authentication.getPrincipal();
        
        String tokenJWT = tokenService.gerarToken(userPrincipal);
        
        UserResponse userResponse = new UserResponse(
            userPrincipal.id(),
            null,
            userPrincipal.username(),
            userPrincipal.username(),
            null,
            userPrincipal.roles()
        );

        return new UserLoginResponse(tokenJWT, userResponse);
    }

    public UUID getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken || auth == null) {
            return null;
        }

        UserSpringSecurity userDetails = (UserSpringSecurity) auth.getPrincipal();
        return userDetails.id();
    }
    
    public void logout() {
        SecurityContextHolder.clearContext();
    }
    
    
}