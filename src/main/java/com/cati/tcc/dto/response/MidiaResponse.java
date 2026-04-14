package com.cati.tcc.dto.response;

import java.util.UUID;

import com.cati.tcc.model.enums.TipoMidia;


public record MidiaResponse(
		
		UUID id,
	    String url, 
	    String nomeArquivo,
	    TipoMidia tipo
		
		) {

}
