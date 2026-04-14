package com.cati.tcc.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cati.tcc.model.enums.StatusCarrinho;

import java.util.List;

public record CarrinhoResponse(
		
		UUID id,
	    UUID idUsuario,
	    Double total,
	    LocalDateTime dataCriacao,
	    StatusCarrinho status,
	    List<ItemCarrinhoResponse> itens
		
		) {

}
