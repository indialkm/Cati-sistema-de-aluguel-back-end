package com.cati.tcc.dto.request;

import com.cati.tcc.model.enums.NivelDeConservacao;
import com.cati.tcc.model.enums.StatusEquipamento;
import com.fasterxml.jackson.annotation.JsonProperty;

public record EquipamentoRequest(
	    @JsonProperty("numeroSerie") String numeroSerie,
	    @JsonProperty("modelo") String modelo,
	    @JsonProperty("cor") String cor,
	    @JsonProperty("observacoesInternas") String observacoesInternas,
	    @JsonProperty("altura") Double altura,
	    @JsonProperty("peso") Double peso,
	    @JsonProperty("preco") Double preco,
	    @JsonProperty("condicao") NivelDeConservacao condicao,
	    @JsonProperty("statusEquipamento") StatusEquipamento statusEquipamento
	) {}