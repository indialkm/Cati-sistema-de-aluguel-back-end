package com.cati.tcc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.dto.request.PagamentoRequest;
import com.cati.tcc.dto.response.AluguelResponse;
import com.cati.tcc.dto.response.PagamentoResponse;
import com.cati.tcc.dto.response.PedidoResponse;
import com.cati.tcc.mapper.PagamentoMapper;
import com.cati.tcc.mapper.PedidoMapper;
import com.cati.tcc.model.Pagamento;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.service.TransacaoService;
import com.cati.tcc.mapper.AluguelMapper;

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
    private final AluguelMapper aluguelMapper;

	public TransacaoController(TransacaoService transacaoService, PedidoMapper pedidoMapper,
			PagamentoMapper pagamentoMapper, AluguelMapper aluguelMapper) {
		this.transacaoService = transacaoService;
		this.pedidoMapper = pedidoMapper;
		this.pagamentoMapper = pagamentoMapper;
		this.aluguelMapper = aluguelMapper;
	}

	@PostMapping
    @PreAuthorize("hasRole('CLIENT')") 
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<PedidoResponse> criarPedido() {
        return ResponseEntity.status(HttpStatus.CREATED).body( pedidoMapper.toResponse(transacaoService.finalizarLocacao()));
    }

    @PostMapping("/pagar")
    @PreAuthorize("hasRole('CLIENT')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<PagamentoResponse> checkout(@RequestBody @Valid PagamentoRequest request) {
      
        Pagamento pagamento = transacaoService.finalizarCheckout(request);
        return ResponseEntity.ok(pagamentoMapper.toResponse(pagamento));
    }
    
    
    
    @PostMapping("/confirmar/teste/{idPedido}")
    public ResponseEntity<List<AluguelResponse>> simularSucessoStripe(@PathVariable("idPedido") UUID idPedido) {
     
        return ResponseEntity.ok(transacaoService.gerarAlugueisTeste(idPedido).stream().map(aluguelMapper::toResponse).toList());
    	
       
    }
    @PostMapping("/confirmar/{stripeId}")
    public ResponseEntity<String> confirmarPagamento(@PathVariable String stripeId) {
        try {
            
            transacaoService.confirmarPagamentoEGerarAluguel(stripeId);
            
            return ResponseEntity.ok("Pagamento confirmado e aluguéis gerados com sucesso!");
            
        } catch (RuntimeException e) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
           
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar confirmação.");
        }
    }
    
    
}
