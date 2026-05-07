package com.cati.tcc.repository;

import com.cati.tcc.config.payment.GatewayReponse;
import com.cati.tcc.dto.request.PagamentoRequest;
import com.cati.tcc.model.enums.StatusPagamento;

public interface PagamentoGateway {
    
    GatewayReponse criarIntencao(PagamentoRequest request);
    
    void estornar(String transacaoId, Double valor);
}