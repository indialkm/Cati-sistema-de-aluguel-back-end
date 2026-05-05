package com.cati.tcc.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	
	@PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
	@PatchMapping("/status-manutencao/{id}")
	public ResponseEntity<EquipamentoResponse> statusManutencao(@PathVariable("id") UUID id){
		
		EquipamentoResponse equipamento = equipamentoMapper.toResponse(equipamentoService.statusManutencao(id));
		
		return ResponseEntity.ok(equipamento);
		
	}
	
	
	@PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
	@GetMapping
	public ResponseEntity<Page<EquipamentoResponse>> buscarTodosEquipamentos(
			@PageableDefault(page = 0, size = 10) Pageable pageable){
		
		return ResponseEntity.ok(equipamentoService.listarTodos(pageable).map(equipamentoMapper::toResponse));
	}

	@PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
	@GetMapping("/pesquisar")
	public ResponseEntity<Page<EquipamentoResponse>> pesquisarPorNome(
			@RequestParam String nome,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		
		return ResponseEntity.ok(equipamentoService.pesquisarPorNome(nome, pageable).map(equipamentoMapper::toResponse));
	}

	@PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
	@GetMapping("/disponibilidade")
	public ResponseEntity<Page<EquipamentoResponse>> filtrarDisponibilidade(
			@PageableDefault(page = 0, size = 10) Pageable pageable) {
		
		return ResponseEntity.ok(equipamentoService.filtrarDisponiveis(pageable).map(equipamentoMapper::toResponse));
	}
	
	@GetMapping("/estoque/{idEstoque}/equipamentos")
	@PreAuthorize("hasRole('OWNER')")
	@SecurityRequirement(name = "bearer-key")
	public ResponseEntity<Page<EquipamentoResponse>> listarEquipamentosPorEstoque(
	        @PathVariable UUID idEstoque,
	        @PageableDefault(size = 10, sort = "id") Pageable paginacao) {

	    return ResponseEntity.ok(
	            equipamentoService.listaEquipamentoPorEstoque(idEstoque, paginacao)
	                    .map(equipamentoMapper::toResponse)
	    );
	}
	
	@PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
    @DeleteMapping("/{idEquipamento}")
    public ResponseEntity<Void> excluir(
            @PathVariable("idEquipamento") UUID idEquipamento) {
        
        equipamentoService.excluirEquipamento(idEquipamento);
        
        return ResponseEntity.noContent().build();
    }
	

}
