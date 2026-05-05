package com.cati.tcc.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.dto.response.EnderecoResponse;
import com.cati.tcc.dto.response.EstoqueResponse;
import com.cati.tcc.mapper.EnderecoMapper;
import com.cati.tcc.service.EnderecoService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/endereco")
@Tag(name = "Endereco")
public class EnderecoController {
	
	private final EnderecoService enderecoService;
	private final EnderecoMapper enderecoMapper;
	
	public EnderecoController(EnderecoService enderecoService, EnderecoMapper enderecoMapper) {
		this.enderecoService = enderecoService;
		this.enderecoMapper = enderecoMapper;
	}

	@GetMapping
	@PreAuthorize("permitAll()")
	public ResponseEntity<Page<EnderecoResponse>> exibirTodosEndereco(
			@PageableDefault(page = 0, size = 10) Pageable pageable)	{	
		Page<EnderecoResponse> enderecoResponse = enderecoService.buscarTodos(pageable)
                .map(enderecoMapper::toResponse);
    	return ResponseEntity.ok(enderecoResponse);
	}
	
}
