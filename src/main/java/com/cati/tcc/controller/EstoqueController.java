package com.cati.tcc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.dto.request.EstoqueRequest;
import com.cati.tcc.dto.request.UserRequest;
import com.cati.tcc.dto.response.EstoqueResponse;
import com.cati.tcc.dto.response.UserResponse;
import com.cati.tcc.mapper.EstoqueMapper;
import com.cati.tcc.service.EstoqueService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/estoque")
@Tag(name = "Estoque")
public class EstoqueController {
	
	private final EstoqueService estoqueService;
    private final EstoqueMapper mapper;

    public EstoqueController(EstoqueService estoqueService, EstoqueMapper mapper) {
        this.estoqueService = estoqueService;
        this.mapper = mapper;
    }
    
    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
	public ResponseEntity<EstoqueResponse> criar(@Valid @RequestBody EstoqueRequest request)
	{
		var estoque = estoqueService.criar(request);
        var response = mapper.toResponse(estoque);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}

}
