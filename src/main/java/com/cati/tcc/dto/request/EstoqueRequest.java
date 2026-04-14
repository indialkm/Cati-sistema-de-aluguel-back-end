package com.cati.tcc.dto.request;

import java.util.List;
import java.util.Optional;

public record EstoqueRequest(
		
		Optional<Integer> quantidade,
		Optional<String> descricao,
		String nome,
		Optional<Double> precoBase,
		String categoria,
		//Double largura,
		//Double comprimento,
		List<String> fotosModelos
		
		) {}
