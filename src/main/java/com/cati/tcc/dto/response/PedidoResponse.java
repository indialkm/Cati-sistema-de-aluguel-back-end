package com.cati.tcc.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.cati.tcc.model.enums.StatusPedido;



public record PedidoResponse(
		
		UUID id,
	    Double valorTotal,
	    StatusPedido status,
	    LocalDateTime dataPedido,
	    List<ItemPedidoResponse> itens
	    
		) {

}
