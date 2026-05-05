package com.cati.tcc.dto.request;

import java.util.UUID;

import com.cati.tcc.model.enums.FormasPagamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PagamentoRequest(
		
				UUID idPedido,
			    Double valorPago,
			    FormasPagamento formaPagamento,
			    Integer parcelas
	
		) {

}
