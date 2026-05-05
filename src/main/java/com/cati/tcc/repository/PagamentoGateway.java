package com.cati.tcc.repository;

import com.cati.tcc.config.payment.GatewayReponse;
import com.cati.tcc.dto.request.PagamentoRequest;
import com.cati.tcc.model.enums.StatusPagamento;

public interface PagamentoGateway {
    // Em vez de retornar Status, retorna o ClientSecret (ou um objeto com o ID da transação)
    GatewayReponse criarIntencao(PagamentoRequest request);
    
    // Novo método para cancelamento
    void estornar(String transacaoId, Double valor);
}