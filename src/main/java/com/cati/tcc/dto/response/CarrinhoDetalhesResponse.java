package com.cati.tcc.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CarrinhoDetalhesResponse(

		UUID idcarrinho,
		List<ItemCarrinhoResponseDet> itens,
		Double valorTotal
		
		
		) {

}
