package com.cati.tcc.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cati.tcc.config.security.UserSpringSecurity;
import com.cati.tcc.dto.request.EnderecoRequest;
import com.cati.tcc.mapper.EnderecoMapper;
import com.cati.tcc.model.Endereco;
import com.cati.tcc.repository.EnderecoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class EnderecoService {

	private final EnderecoMapper enderecoMapper;
	private final EnderecoRepository enderecoRepository;
	
	
	
	public EnderecoService(EnderecoMapper enderecoMapper, EnderecoRepository enderecoRepository) {
		this.enderecoMapper = enderecoMapper;
		this.enderecoRepository = enderecoRepository;
		
	}

	@Transactional
	public Endereco preparar(EnderecoRequest request) {
        return enderecoMapper.toEntity(request);
    }
	
	public Endereco buscarId(UUID id) {
		return enderecoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Endereco nao encontrado"));
	}
	
	
}
