package com.cati.tcc.service;

import com.cati.tcc.config.payment.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cati.tcc.config.exceptions.ResourceNotFoundException;
import com.cati.tcc.dto.request.PagamentoRequest;
import com.cati.tcc.dto.response.PagamentoResponse;
import com.cati.tcc.mapper.PagamentoMapper;
import com.cati.tcc.model.Pagamento;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.enums.StatusPagamento;
import com.cati.tcc.repository.PagamentoGateway;
import com.cati.tcc.repository.PagamentoRepository;
import com.stripe.model.PaymentIntent;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PagamentoService {

	private final PagamentoGateway gateway;
	private final PagamentoRepository pagamentoRepository;
    private final PagamentoMapper mapper;
    private final PedidoService pedidoService;

   public PagamentoService(PagamentoGateway gateway, PagamentoRepository pagamentoRepository, PagamentoMapper mapper,
			PedidoService pedidoService) {
		this.gateway = gateway;
		this.pagamentoRepository = pagamentoRepository;
		this.mapper = mapper;
		this.pedidoService = pedidoService;
	}
    
    @Transactional
    public Pagamento iniciarPagamento(PagamentoRequest request) {
        
    	Pedido pedido = pedidoService.buscarPorId(request.idPedido());		
        
    	GatewayReponse payment = gateway.criarIntencao(request);

        
        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setValorPago(request.valorPago());
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setTransacaoGatewayId(payment.getIdTransacao());
        pagamento.setClientSecret(payment.getClientSecret());
        pagamento.setFormaPagamento(request.formaPagamento());
        
        pagamento = pagamentoRepository.save(pagamento);
       
        return pagamento;
    }
    
    @Transactional
    public void pagamentoAprovado(Pagamento pagamento) {
    	pagamentoRepository.save(pagamento);
    	
    }
    
    public String pegarFormaDepagamento(UUID idPedido) {

    	Pagamento pag = pagamentoRepository.findByPedidoId(idPedido)
    					.orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrando"));
    	
    	return pag.getFormaPagamento().name();
    }
    
    public Pagamento buscarPorExternalId(String externalId) {
        return pagamentoRepository.findBytransacaoGatewayId(externalId)
            .orElseThrow(() -> new EntityNotFoundException("Pagamento não localizado: " + externalId));
    }
    
    @Transactional
    public Pagamento buscarPorpedido(UUID pedido) {
    	return pagamentoRepository.findByPedidoId(pedido)
    			  .orElseThrow(() -> new EntityNotFoundException("Pagamento não localizado"));
    }
    
    public Optional<Pagamento> buscarOpcionalPorPedidoId(UUID pedidoId) {
        return pagamentoRepository.findByPedidoId(pedidoId);
    }
    
    @Transactional
    public void cancelarPedidoNaoPago(UUID pagamentoId) {
        Pagamento p = pagamentoRepository.findById(pagamentoId).get();
        p.setStatus(StatusPagamento.CANCELADO); 
        pagamentoRepository.save(p);
    }

    public void realizarEstorno(UUID pagamentoId) {
    	Pagamento p = pagamentoRepository.findById(pagamentoId)
    		    .orElseThrow(() -> new EntityNotFoundException("Pagamento com ID " + pagamentoId + " não encontrado para estorno."));
        
        
        gateway.estornar(p.getTransacaoGatewayId(), p.getValorPago());
        
        p.setStatus(StatusPagamento.ESTORNADO);
        pagamentoRepository.save(p);
    }
	
}
