package com.cati.tcc.dto.response;

import java.util.UUID;

public record ItemPedidoResponse(
		UUID id,
	    UUID estoqueId,
	    String nomeProduto,
	    UUID reservaId,
	    Double valorCobrado,
	    UUID enderecoEntregaId
	
		
		) {}
