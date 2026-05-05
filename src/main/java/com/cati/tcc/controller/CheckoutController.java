package com.cati.tcc.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.dto.request.ChecklistRequest;
import com.cati.tcc.dto.response.PedidoResponse;
import com.cati.tcc.mapper.PedidoMapper;
import com.cati.tcc.service.CheckoutService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/checkout")
@Tag(name = "Checkout")
public class CheckoutController {

	private final PedidoMapper pedidoMapper;
	private final CheckoutService checkoutService;
	

	public CheckoutController(PedidoMapper pedidoMapper, CheckoutService chekcoutServuce) {
		this.pedidoMapper = pedidoMapper;
		this.checkoutService = chekcoutServuce;
	}


	@PatchMapping("{idPedido}")
	@PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
	public Boolean checkout(@Valid @PathVariable UUID idPedido){
		
		return checkoutService.checkout(idPedido);
		
		
	} 
	
}
