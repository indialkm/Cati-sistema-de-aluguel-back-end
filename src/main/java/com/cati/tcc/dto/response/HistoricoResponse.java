package com.cati.tcc.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cati.tcc.model.enums.StatusPagamento;

public record HistoricoResponse(
	    UUID id,
	    LocalDateTime dataEvento,
	    String descricao,
	    StatusPagamento status,
	    PagamentoResponse pagamento,
	    UUID pedidoId,
	    UserResponse user 
	) {}