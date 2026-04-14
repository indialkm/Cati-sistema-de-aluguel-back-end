package com.cati.tcc.dto.request;

import java.util.UUID;

public record ItemPedidoRequest(
	
	    UUID reservaId,
	    UUID enderecoEntregaId,
	    Double valorCobrado
	    
		
		) {

}
