package com.cati.tcc.dto.request;


public record EnderecoRequest(
		
		String cep,
		String logradouro,
		String numero,
		String complemento,
		String bairro,
	    String cidade,
	    String uf
		
		) {}
