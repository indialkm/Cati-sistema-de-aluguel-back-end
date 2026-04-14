package com.cati.tcc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.dto.response.ItemPedidoResponse;
import com.cati.tcc.mapper.ItemPedidoMapper;
import com.cati.tcc.service.ItemPedidoService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/itempedido")
@Tag(name = "Item Pedido")
public class ItemPedidoController {
	
	private final ItemPedidoService itemService;
	private final ItemPedidoMapper itemMapper;
	
	public ItemPedidoController(ItemPedidoService itemService, ItemPedidoMapper itemMapper) {
		this.itemService = itemService;
		this.itemMapper = itemMapper;
	}
	
	@GetMapping("/{id}/itens")
	public ResponseEntity<Page<ItemPedidoResponse>> listarItensDoPedido(
	        @PathVariable UUID idPedido, 
	        @PageableDefault(size = 10) Pageable pageable) {
	    
	    return ResponseEntity.ok(itemService.buscarItensDoPedidoPaginado(idPedido, pageable)
	    		.map(itemMapper::toResponse));
	}
	
	
	@GetMapping
	@PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
	public ResponseEntity<List<ItemPedidoResponse>> listatTodos(){
		return ResponseEntity.ok(itemService.buscarTodos().stream()
				.map(item -> itemMapper.toResponse(item))
				.toList());
	}
	

}
