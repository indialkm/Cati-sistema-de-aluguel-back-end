package com.cati.tcc.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cati.tcc.dto.request.EquipamentoRequest;
import com.cati.tcc.mapper.EquipamentoMapper;
import com.cati.tcc.model.Equipamento;
import com.cati.tcc.model.Estoque;
import com.cati.tcc.model.enums.StatusEquipamento;
import com.cati.tcc.repository.EquipamentoRepository;

import jakarta.persistence.EntityNotFoundException;
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
		
		
		estoque.adicionarEquipamento(equipamento);
		
		equipamentoRepository.save(equipamento);
		
		estoqueService.retornarEstoque(estoqueId, 1);
		
		
		return equipamento;
	}
	
	@Transactional
	public Equipamento buscarId(UUID id) {
		return equipamentoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado"));
	}
	
	@Transactional
	public void excluirEquipamento(UUID equipamentoId) {
		Equipamento equipamento = buscarId(equipamentoId);
		Estoque estoque = estoqueService.buscarPorId(equipamento.getEstoque().getId());

	    estoque.getEquipamentos().removeIf(e -> e.getId().equals(equipamentoId));
	    
	    estoqueService.atualizar(estoque);
	}
	
	@Transactional
	public Page<Equipamento> listarTodos(Pageable pageable){
		
		return equipamentoRepository.findAll(pageable);
		
	}
	
	@Transactional
	public Equipamento statusManutencao(UUID idEquipamento)
	{
		Equipamento equipamento = buscarId(idEquipamento);
		
		equipamento.setStatusEquipamento(StatusEquipamento.MANUTENCAO);
		
		return equipamentoRepository.save(equipamento);
	}
	
	// Buscar por modelo (já que você não tem campo 'nome', usei 'modelo')
    public Page<Equipamento> pesquisarPorNome(String nome, Pageable pageable) {
        return equipamentoRepository.findByModeloContainingIgnoreCase(nome, pageable);
    }

    // Filtrar apenas os DISPONIVEIS
    public Page<Equipamento> filtrarDisponiveis(Pageable pageable) {
        return equipamentoRepository.findByStatusEquipamento(StatusEquipamento.DISPONIVEL, pageable);
    }
    
   
    public Page<Equipamento> listaEquipamentoPorEstoque(UUID idEstoque, Pageable pageable) {
        return equipamentoRepository.findByEstoqueId(idEstoque, pageable);
    }
  
}
