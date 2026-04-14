package com.cati.tcc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cati.tcc.dto.request.EquipamentoRequest;
import com.cati.tcc.dto.request.EstoqueRequest;
import com.cati.tcc.dto.response.EstoqueResponse;
import com.cati.tcc.mapper.EstoqueMapper;
import com.cati.tcc.model.Equipamento;
import com.cati.tcc.model.Estoque;
import com.cati.tcc.repository.EstoqueRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class EstoqueService {
	
	private final EstoqueRepository estoqueRepository;
    private final EstoqueMapper estoqueMapper;
    

	public EstoqueService(EstoqueRepository estoqueRepository, EstoqueMapper estoqueMapper) {
		this.estoqueRepository = estoqueRepository;
		this.estoqueMapper = estoqueMapper;
	}


	@Transactional
    public Estoque criar(EstoqueRequest request) {
		
		if(estoqueRepository.existsByNome(request.nome())){
	        throw new IllegalArgumentException("Já existe um modelo de estoque cadastrado com este nome.");
	    }
	    
	    Estoque estoque = estoqueMapper.toEntity(request);
	    return estoqueRepository.save(estoque);
    }
	
	
	
	
	public Estoque atualizar(Estoque estoqueEntidade){
		
		return estoqueRepository.save(estoqueEntidade);
			
	}
	
	public Estoque buscarId(UUID id) {
		return estoqueRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Estoque não encontrado"));
	}
	
	public List<Estoque> verificarDisponibilidadeParaReserva(UUID id, LocalDateTime inicio, LocalDateTime fim) {
        return estoqueRepository.buscarEstoquesDisponiveis(id, inicio, fim);
    }
    
}
