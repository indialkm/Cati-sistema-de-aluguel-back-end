package com.cati.tcc.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cati.tcc.model.enums.NivelDeConservacao;
import com.cati.tcc.model.enums.StatusEquipamento;

public record EquipamentoResponse(
		
		UUID id,
		String numeroSerie,
	    String modelo,
	    String cor,
	    String observacoesInternas,
	    double altura,
	    double peso,
	    double preco,
	    LocalDateTime dataCricao,
	    NivelDeConservacao condicao,
	    StatusEquipamento statusEquipamento
	    
		) {}
