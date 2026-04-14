package com.cati.tcc.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record NotaPedidoResponse(
		
		// Dados do Emitente (Fornecedor)
	    String emitenteRazaoSocial,
	    String emitenteCnpj,
	    String emitenteInscricao,
	    String emitenteEndereco,

	    // Dados do Tomador (Cliente)
	    String clienteNome,
	    String clienteCpfCnpj,
	    String clienteEndereco,

	    // Dados do Pedido
	    Double valorTotal,
	    LocalDateTime dataEmissao

		) {

}
