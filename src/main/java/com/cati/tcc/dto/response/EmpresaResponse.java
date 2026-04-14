package com.cati.tcc.dto.response;

public record EmpresaResponse(
		
		Long id,
	    String razaoSocial,
	    String cnpj,
	    String inscricaoEstadual,
	    String endereco,
	    String telefone,
	    String email,
	    String nomeDono
		
		
		) {

}
