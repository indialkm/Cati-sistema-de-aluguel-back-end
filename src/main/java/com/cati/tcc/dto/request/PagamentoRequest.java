package com.cati.tcc.dto.request;

import java.util.UUID;

import com.cati.tcc.model.enums.FormasPagamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PagamentoRequest(
		
	    UUID idPedido,
	    FormasPagamento formaPagamento,
	    String transacaoGatewayId,
	    Double valorPago,
	    String payload_retorno,
	    Integer parcelas
		
		) {

}
