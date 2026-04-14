package com.cati.tcc.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cati.tcc.dto.request.EnderecoRequest;
import com.cati.tcc.dto.request.UserRequest;
import com.cati.tcc.mapper.UserMapper;
import com.cati.tcc.model.Endereco;
import com.cati.tcc.model.User;
import com.cati.tcc.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	
	/**Segurança**/
	private final PasswordEncoder passwordEncoder;
	
	
	/**User = root, Endereco = agregado**/
	private final EnderecoService enderecoService;
	private final AuthService authService;


	public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder,
			EnderecoService enderecoService, AuthService authService) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
		this.enderecoService = enderecoService;
		this.authService = authService;
	}



	@Transactional
	public User save(UserRequest request) {
		
		if(userRepository.existsByEmail(request.email())) {
			throw new RuntimeException("Email já cadastrado");
		};
		System.out.println(request.email());
		/*
		if(userRepository.existsByCNPJ(request.cnpj())) {
			throw new RuntimeException("CNPJ já cadastrado");
		}
		
		if(userRepository.existsByCPF(request.cpf())) {
			throw new RuntimeException("CPF já cadastrado");
		}*/
		
		User user = userMapper.toEntity(request);
		user.setPassword(this.passwordEncoder.encode(request.password()));
		return userRepository.save(user);
	}
	
	
	
	public User buscarId(UUID id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
	}
	
	
	/****** ENDERECO METODOS **********/
	
	@Transactional	
	public Endereco vincularEnderecoUser(EnderecoRequest request) {
		
		UUID idUser = authService.getAuthenticatedUserId();
		
		User user = userRepository.findById(idUser)
					.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

		Endereco endereco = enderecoService.preparar(request);
		user.adicionarEndereco(endereco);
		userRepository.save(user);
		
		
		
		return user.getEnderecos().stream()
		        .filter(e -> e.getLogradouro().equals(endereco.getLogradouro()) && e.getCep().equals(endereco.getCep()) && e.getNumero().equals(endereco.getNumero())) 
		        .findFirst()
		        .orElse(endereco);
	}
	
	
	/*public User procurarDonoEmpresa(String role) {
	    return userRepository.buscarPrimeiroComCnpj("OWNER")
	            .orElseThrow(() -> new EntityNotFoundException(
	                "Configuração da empresa não encontrada: É necessário um usuário com perfil OWNER e CNPJ cadastrado."));
	}*/
	

}
