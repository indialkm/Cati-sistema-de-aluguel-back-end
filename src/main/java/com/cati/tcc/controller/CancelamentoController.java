package com.cati.tcc.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.dto.response.CancelamentoResponse;
import com.cati.tcc.service.CancelamentoService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cancelamento")
@Tag(name="Cancelamento")
public class CancelamentoController {
	
	private final CancelamentoService cancelamentoService;

    
    public CancelamentoController(CancelamentoService cancelamentoService) {
        this.cancelamentoService = cancelamentoService;
    }

    
    @PostMapping("/{pedidoId}/cancelar")
    public ResponseEntity<CancelamentoResponse> cancelarPedido(@PathVariable UUID pedidoId) {
       
       String mensagem = cancelamentoService.executarCancelamentoTotal(pedidoId);
     
        CancelamentoResponse resposta = new CancelamentoResponse(
            mensagem,
            pedidoId,
            "CANCELADO",
            "ESTORNADO",
            LocalDateTime.now()
        );

        return ResponseEntity.ok(resposta);
    }

}
