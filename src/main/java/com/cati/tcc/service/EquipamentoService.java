package com.cati.tcc.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cati.tcc.dto.request.EquipamentoRequest;
import com.cati.tcc.mapper.EquipamentoMapper;
import com.cati.tcc.model.Equipamento;
import com.cati.tcc.model.Estoque;
import com.cati.tcc.repository.EquipamentoRepository;

import jakarta.transaction.Transactional;

@Service
public class EquipamentoService {
	
	private final EquipamentoRepository equipamentoRepository;
	private final EquipamentoMapper equipamentoMapper;
	private final EstoqueService estoqueService;
	

	public EquipamentoService(EquipamentoRepository equipamentoRepository, EquipamentoMapper equipamentoMapper,
			EstoqueService estoqueService) {
		this.equipamentoRepository = equipamentoRepository;
		this.equipamentoMapper = equipamentoMapper;
		this.estoqueService = estoqueService;
	}

	@Transactional
	public Equipamento criar(UUID estoqueId, EquipamentoRequest request) {
		
		Estoque estoque = estoqueService.buscarId(estoqueId);
		
		Equipamento equipamento = equipamentoMapper.toEntity(request);
		
		equipamentoRepository.save(equipamento);
		
		estoque.adicionarEquipamento(equipamento);
		
		estoqueService.atualizar(estoque);
		
		return equipamento;
	}
	
	@Transactional
	public void excluirEquipamento(UUID estoqueId, UUID equipamentoId) {
	    Estoque estoque = estoqueService.buscarId(estoqueId);
	    
	   
	    estoque.getEquipamentos().removeIf(e -> e.getId().equals(equipamentoId));
	    
	    estoqueService.atualizar(estoque);
	}
	
	

}
