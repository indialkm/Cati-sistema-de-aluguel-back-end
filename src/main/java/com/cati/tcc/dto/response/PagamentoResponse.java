package com.cati.tcc.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cati.tcc.model.enums.FormasPagamento;
import com.cati.tcc.model.enums.StatusPagamento;


public record PagamentoResponse(
		
		UUID id,
	    UUID idPedido,
	    Double valorPago,
	    LocalDateTime dataPagamento,
	    StatusPagamento status,
	    FormasPagamento formaPagamento,
	    String transacaoGatewayId,
	    Integer parcelas
		
		) {

}
