package com.cati.tcc.dto.request;

import java.util.List;
import java.util.Optional;

public record EstoqueRequest(
		
		String descricao,
		String nome,
		Double altura,
		Double largura,
		Double precoBase,
		String categoria,
		String tipoEstoque
		
		) {}
