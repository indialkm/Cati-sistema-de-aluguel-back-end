package com.cati.tcc.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.dto.response.PedidoResponse;
import com.cati.tcc.mapper.PedidoMapper;
import com.cati.tcc.model.enums.StatusPedido;
import com.cati.tcc.service.PedidoService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/pedido")
@Tag(name = "Pedido")
public class PedidoController {
	
	private final PedidoService pedidoService;
	private final PedidoMapper pedidoMapper;
	
	public PedidoController(PedidoService pedidoService, PedidoMapper pedidoMapper) {
		this.pedidoService = pedidoService;
		this.pedidoMapper = pedidoMapper;
	}


	@GetMapping
	@PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
	public ResponseEntity<Page<PedidoResponse>> buscarTodos(
			@PageableDefault(page = 0, size = 10) Pageable pageable){
		
		return ResponseEntity.ok(pedidoService.buscarTodos(pageable).map(pedidoMapper::toResponse));
	}
	
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('CLIENT')")
    @SecurityRequirement(name = "bearer-key")
	public ResponseEntity<PedidoResponse> buscarPedidoId(@PathVariable("id") UUID idPedido){
		
		return ResponseEntity.ok(pedidoMapper.toResponse(pedidoService.buscarPorId(idPedido))); 
		
	}
	
	@GetMapping("/buscarAluguel")
	@PreAuthorize("hasRole('CLIENT')")
    @SecurityRequirement(name = "bearer-key")
	public ResponseEntity<Page<PedidoResponse>> buscarPedidoPorUsuario(
			@PageableDefault(page = 0, size = 10) Pageable pageable){
		
		return ResponseEntity.ok(pedidoService.buscarPorUsuario(pageable).map(pedidoMapper::toResponse));
	}
	
	@GetMapping("/status")
	@PreAuthorize("hasRole('CLIENT') or hasRole('OWNER')") 
	@SecurityRequirement(name = "bearer-key")
	public ResponseEntity<Page<PedidoResponse>> buscarPorStatusPedido(
	        @RequestParam(value = "status", required = false) StatusPedido status, 
	        @PageableDefault(page = 0, size = 10) Pageable pageable) {
	    
	    return ResponseEntity.ok(pedidoService.buscarPorStatus(status, pageable).map(pedidoMapper::toResponse));
	}
}
