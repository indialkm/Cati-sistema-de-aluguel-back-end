package com.cati.tcc.dto.response;

import java.util.List;
import java.util.UUID;

public record EstoqueResponse(
	    UUID id,
	    int quantidade,
	    String descricao,
	    String nome,
	    double precoBase,
	    List<MidiaResponse> fotosModelos, 
	    String categoria,
	    List<EquipamentoResponse> equipamentos,
	    Double largura,
	    Double altura,
	    String tipoEstoque
	    
	) {}
