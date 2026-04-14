package com.cati.tcc.repository;

import com.cati.tcc.dto.request.PagamentoRequest;
import com.cati.tcc.model.enums.StatusPagamento;

public interface PagamentoGateway {
	StatusPagamento processar(PagamentoRequest request);
}
