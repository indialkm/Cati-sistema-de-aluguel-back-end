package com.cati.tcc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.config.security.UserSpringSecurity;
import com.cati.tcc.dto.request.DadosAutenticacao;
import com.cati.tcc.dto.response.DadosTokenJWT;
import com.cati.tcc.dto.response.UserLoginResponse;
import com.cati.tcc.dto.response.UserResponse;
import com.cati.tcc.mapper.UserMapper;
import com.cati.tcc.service.AuthService;
import com.cati.tcc.service.TokenService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
@Tag(name = "Login")
public class AutenticacaoController {
	
	private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final AuthService authService;

    public AutenticacaoController(AuthenticationManager manager, TokenService tokenService, UserMapper userMapper,
			AuthService authService) {
		this.manager = manager;
		this.tokenService = tokenService;
		this.userMapper = userMapper;
		this.authService = authService;
	}


    @PostMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserLoginResponse> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        UserLoginResponse response = authService.autenticar(dados);
        return ResponseEntity.ok(response);
    }

	
}
