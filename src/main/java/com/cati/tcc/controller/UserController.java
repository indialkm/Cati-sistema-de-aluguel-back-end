package com.cati.tcc.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.config.security.UserSpringSecurity;
import com.cati.tcc.dto.request.EnderecoRequest;
import com.cati.tcc.dto.request.UserRequest;
import com.cati.tcc.dto.response.EmpresaResponse;
import com.cati.tcc.dto.response.EnderecoResponse;
import com.cati.tcc.dto.response.UserResponse;
import com.cati.tcc.mapper.EnderecoMapper;
import com.cati.tcc.mapper.UserMapper;
import com.cati.tcc.model.User;
import com.cati.tcc.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
@Tag(name = "User")
public class UserController {
	
	private final UserService userService;
	private final UserMapper userMapper;
	private final EnderecoMapper enderecoMapper;

	public UserController(UserService userService, UserMapper userMapper, EnderecoMapper enderecoMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
		this.enderecoMapper = enderecoMapper;
	}

	@PostMapping
	@PreAuthorize("permitAll()")
	public ResponseEntity<UserResponse> criar(@Valid @RequestBody UserRequest request)
	{
		var user = userService.save(request);
        var response = userMapper.toResponse(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
	
	/****Endeco Metodos***/
	
	@GetMapping 
	@PreAuthorize("permitAll()") 
	public List<UserResponse> listarTodos(){
		
		return userService.listarTodos().stream().map(userMapper::toResponse).toList(); 
		
	}
	
	@PostMapping("/me/endereco")
	public ResponseEntity<EnderecoResponse> adicionarEndereco(@Valid @RequestBody EnderecoRequest request) {
	    return ResponseEntity.ok(enderecoMapper.toResponse(userService.salvarEnderecoStateless(request)));
	}
	
	@PreAuthorize("hasAnyRole('CLIENT', 'OWNER')")
    @SecurityRequirement(name = "bearer-key")
    @PutMapping("/meu-perfil")
    public ResponseEntity<UserResponse> atualizarPerfil(@RequestBody @Valid UserRequest request) {
        
        return ResponseEntity.ok(userMapper.toResponse(userService.atualizarPerfil(request)));
    }
	
	@GetMapping("/me/endereco/list") 
	@PreAuthorize("permitAll()") 
	public List<EnderecoResponse> listarMeusEnderecos() {
        
        return userService.exibirEnderecosPorId()
                .stream()
                .map(enderecoMapper::toResponse)
                .toList();
                
    }
	
}
