package com.cati.tcc.dto.response;

import java.util.List;
import java.util.UUID;

public record DetalhesModeloResponse(
	    UUID idModelo,
	    String nome,
	    String descricao,
	    String imagemPrincipalUrl,
	    List<String> imagensUrls, 
	    Double precoDiaria,
	    Double altura, 
	    Double comprimento, 
	    Double avaliacaoMedia,
	    String categoria, 
	    boolean disponivel
	) {}
