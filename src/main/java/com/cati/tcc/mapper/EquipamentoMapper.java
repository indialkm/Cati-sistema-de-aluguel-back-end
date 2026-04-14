package com.cati.tcc.mapper;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.EquipamentoRequest;
import com.cati.tcc.dto.response.EquipamentoResponse;
import com.cati.tcc.model.Equipamento;

@Component
public class EquipamentoMapper {
	
public Equipamento toEntity(EquipamentoRequest request) {
		System.out.println("DEBUG REQUEST: " + request);
		Equipamento equipamento = new Equipamento();
		equipamento.setNumeroSerie(request.numeroSerie());
		equipamento.setModelo(request.modelo());
		equipamento.setCor(request.cor());
		equipamento.setObservacoesInternas(request.observacoesInternas());
		equipamento.setAltura(request.altura() != null ? request.altura() : 0.0);;
		equipamento.setPeso(request.peso() != null ? request.peso() : 0.0);
		equipamento.setPreco(request.preco() != null ? request.preco() : 0.0);
		equipamento.setCondicao(request.condicao());
		equipamento.setStatusEquipamento(request.statusEquipamento());
		
		return equipamento;
	}
	
	
	public EquipamentoResponse toResponse(Equipamento equipamento)
	{
		
		return new EquipamentoResponse(
				equipamento.getId(),
				equipamento.getNumeroSerie(),
				equipamento.getModelo(),
				equipamento.getCor(),
				equipamento.getObservacoesInternas(),
				equipamento.getAltura(),
				equipamento.getPeso(),
				equipamento.getPreco(),
				equipamento.getDataCriacao(),
				equipamento.getCondicao(),
				equipamento.getStatusEquipamento()		
				);
				
	}
	
	

}
