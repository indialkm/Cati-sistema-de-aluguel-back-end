package com.cati.tcc.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cati.tcc.config.security.UserSpringSecurity;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    // 1. Método para Gerar o Token
    public String gerarToken(UserSpringSecurity usuario) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            
            return Jwts.builder()
                    .issuer("API TCC Cati")
                    .subject(usuario.username())
                    .claim("id", usuario.id().toString()) 
                    .expiration(Date.from(dataExpiracao()))
                    .signWith(key)
                    .compact();
        } catch (Exception exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // 2. Método para Validar e pegar o e-mail (Subject) do Token
    public String getSubject(String tokenJWT) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(tokenJWT)
                    .getPayload()
                    .getSubject();
        } catch (JwtException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    // 3. Define quanto tempo o token vale (ex: 2 horas)
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}