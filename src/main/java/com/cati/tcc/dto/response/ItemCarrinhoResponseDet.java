package com.cati.tcc.dto.response;

public record ItemCarrinhoResponseDet(
		
		String nomeModelo,
	    Double precoUnitario,
	    String dataInicial,
	    String dataFinal,
	    String fotoUrl,
	    String enderecoLogradouro,
	    String numero,
	    String bairro,
	    String cidade,
	    String UF
		
		) {

}
