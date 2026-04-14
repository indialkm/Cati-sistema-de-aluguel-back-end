package com.cati.tcc.dto.request;

import java.util.UUID;

public record PedidoRequest(
		
		UUID idCarrinho,
	    UUID idUsuario
		
		) {

}
