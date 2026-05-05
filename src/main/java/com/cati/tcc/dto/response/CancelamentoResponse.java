package com.cati.tcc.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CancelamentoResponse(
	    String mensagem,
	    UUID pedidoId,
	    String novoStatusPedido,
	    String novoStatusPagamento,
	    LocalDateTime dataCancelamento
	) {}
