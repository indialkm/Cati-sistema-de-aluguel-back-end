package com.cati.tcc.dto.response;

import java.util.List;
import java.util.UUID;

public record EstoqueResponse(
		
		UUID id,
		int quantidade,
		String descricao,
		String nome,
		double precoBase,
		List<String> fotosModelos,
		String categoria,
		//Double largura,
		//Double comprimento,
		List<EquipamentoResponse> equipamentos
		
		) {}
