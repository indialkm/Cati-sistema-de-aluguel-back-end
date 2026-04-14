package com.cati.tcc.dto.response;

import java.util.UUID;

import com.cati.tcc.model.enums.StatusAluguel;

public record AluguelResponse(
		
		UUID id,
	    String nomeProduto,
	    Double precoNoAto,
	    StatusAluguel status,
	    ReservaResponse reserva,   
	    EnderecoResponse localizacao
		
		) {

}
