package com.cati.tcc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.dto.request.PagamentoRequest;
import com.cati.tcc.dto.response.PagamentoResponse;
import com.cati.tcc.dto.response.PedidoResponse;
import com.cati.tcc.mapper.PagamentoMapper;
import com.cati.tcc.mapper.PedidoMapper;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.service.TransacaoService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transacao")
@Tag(name = "Transacao")
public class TransacaoController {

    private final TransacaoService transacaoService;
    private final PedidoMapper pedidoMapper;
    private final PagamentoMapper pagamentoMapper;

	

	public TransacaoController(TransacaoService checkoutService, PedidoMapper pedidoMapper,
			PagamentoMapper pagamentoMapper) {
		this.transacaoService = checkoutService;
		this.pedidoMapper = pedidoMapper;
		this.pagamentoMapper = pagamentoMapper;
	}

	@PostMapping
    @PreAuthorize("hasRole('CLIENT')") 
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<PedidoResponse> criarPedido() {
        return ResponseEntity.status(HttpStatus.CREATED).body( pedidoMapper.toResponse(transacaoService.finalizarLocacao()));
    }

    @PostMapping("/transacao")/*Quando é finalizado o pagamento e vira alugueis*/
    @PreAuthorize("hasRole('CLIENT')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<PagamentoResponse> pagar(@RequestBody @Valid PagamentoRequest request) {
        return ResponseEntity.ok(pagamentoMapper.toResponse(transacaoService.finalizarCheckout(request)));
    }
}
