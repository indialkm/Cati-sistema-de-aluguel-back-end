package com.cati.tcc.dto.request;

import java.util.UUID;


public record ItemCarrinhoRequest(
		UUID idReserva,
	    UUID idEndereco
		) {

}
