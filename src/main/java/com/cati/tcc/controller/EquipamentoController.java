package com.cati.tcc.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.dto.request.EquipamentoRequest;
import com.cati.tcc.dto.response.EquipamentoResponse;
import com.cati.tcc.mapper.EquipamentoMapper;
import com.cati.tcc.service.EquipamentoService;

import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/equipamentos")
public class EquipamentoController {
	
	private final EquipamentoService equipamentoService;
	private final EquipamentoMapper equipamentoMapper;
	
	public EquipamentoController(EquipamentoService equipamentoService, EquipamentoMapper equipamentoMapper) {
		this.equipamentoService = equipamentoService;
		this.equipamentoMapper = equipamentoMapper;
	}
    
	// CREATE
	@PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
    @PostMapping("/estoque/{idEstoque}")
    public ResponseEntity<EquipamentoResponse> criar(
            @PathVariable("idEstoque") UUID idEstoque,
            @RequestBody @Valid EquipamentoRequest request) {
        
        return ResponseEntity.ok( equipamentoMapper.toResponse(equipamentoService.criar(idEstoque, request)));
    }
	
	

}
