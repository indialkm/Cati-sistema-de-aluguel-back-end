package com.cati.tcc.config.utils;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.PagamentoRequest;
import com.cati.tcc.model.enums.StatusPagamento;
import com.cati.tcc.repository.PagamentoGateway;

@Component
public class SimuladorPagamentoGateway implements PagamentoGateway {
	
	@Override
    public StatusPagamento processar(PagamentoRequest request) {
      
        if ("token-invalido".equals(request.transacaoGatewayId())) {
            return StatusPagamento.RECUSADO;
        }
        return StatusPagamento.APROVADO;
    }

}
