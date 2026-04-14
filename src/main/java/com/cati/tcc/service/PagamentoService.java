package com.cati.tcc.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.cati.tcc.dto.request.PagamentoRequest;
import com.cati.tcc.dto.response.PagamentoResponse;
import com.cati.tcc.mapper.PagamentoMapper;
import com.cati.tcc.model.Pagamento;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.enums.StatusPagamento;
import com.cati.tcc.repository.PagamentoGateway;
import com.cati.tcc.repository.PagamentoRepository;

import jakarta.transaction.Transactional;

@Service
public class PagamentoService {

	private final PagamentoGateway gateway;
	private final PagamentoRepository repository;
    private final PagamentoMapper mapper;
	
    public PagamentoService(PagamentoGateway gateway, PagamentoRepository repository, PagamentoMapper mapper) {
		this.gateway = gateway;
		this.repository = repository;
		this.mapper = mapper;
	}
    
    @Transactional
    public Pagamento processar(PagamentoRequest request, Pedido pedido) {
      
        StatusPagamento statusGerado = gateway.processar(request);

        Pagamento pagamento = mapper.toEntity(request, pedido);
       
        pagamento.setStatus(statusGerado);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setValorPago(request.valorPago());
        pagamento.setPayload_retorno(request.payload_retorno());

        return repository.save(pagamento);
    }
    
	
}
