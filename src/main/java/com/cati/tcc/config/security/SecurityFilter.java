package com.cati.tcc.config.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cati.tcc.model.User;
import com.cati.tcc.repository.UserRepository;
import com.cati.tcc.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component 
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String tokenJWT = recuperarToken(request);

        // SÓ tenta validar se o token existir!
        if (tokenJWT != null && !tokenJWT.trim().isEmpty()) { 
            try {
                String subject = tokenService.getSubject(tokenJWT);
                
                if (subject != null) {
                    var usuario = userRepository.findByEmail(subject); 

                    if (usuario.isPresent()) {
                        User user = usuario.get();
                        UserSpringSecurity userDetails = new UserSpringSecurity(
                            user.getEmail(), 
                            user.getPassword(), 
                            user.getId(), 
                            user.getRoles()
                        );

                        var authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (Exception e) {
                
                System.err.println("Erro ao validar token: " + e.getMessage());
            }
        }

        // ESSA LINHA PRECISA ESTAR FORA DO IF E DO TRY/CATCH
        filterChain.doFilter(request, response);
    }
    
    

    private String recuperarToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "").trim();
        }
        return null;
    }
}