package com.cati.tcc.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cati.tcc.config.exceptions.NegocioException;
import com.cati.tcc.dto.request.ChecklistRequest;
import com.cati.tcc.dto.response.AluguelResponse;
import com.cati.tcc.mapper.AluguelMapper;
import com.cati.tcc.model.Aluguel;
import com.cati.tcc.model.ItemPedido;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.enums.StatusAluguel;
import com.cati.tcc.model.enums.StatusItemPedido;
import com.cati.tcc.repository.AluguelRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AluguelService {
	
	private final AluguelMapper aluguelMapper;
	private final AluguelRepository aluguelRepository;
	
	private final ItemPedidoService itemService;
	private final ChecklistService checklistService;
	
	public AluguelService(AluguelMapper aluguelMapper, AluguelRepository aluguelRepository,
			ItemPedidoService itemService, ChecklistService checklist) {
		this.aluguelMapper = aluguelMapper;
		this.aluguelRepository = aluguelRepository;
		this.itemService = itemService;
		this.checklistService = checklist;
	}


	public List<Aluguel> gerarAlugueisParaPedido(List<ItemPedido> itens) {
    
        Queue<ItemPedido> filaItens = new LinkedList<>(itens);
        List<Aluguel> alugueisProcessados = new ArrayList<>();

        while (!filaItens.isEmpty()) {
            ItemPedido item = filaItens.poll(); 

            Aluguel novoAluguel = new Aluguel();
           
            novoAluguel.setItemPedido(item);
            
            alugueisProcessados.add(aluguelRepository.save(novoAluguel));
        }

        return alugueisProcessados.stream()
        		.peek(i -> aluguelRepository.save(i))
        		.toList();
    }
	
	
	public Page<Aluguel> listarTodos(Pageable paginacao) {
       
        Page<Aluguel> alugueis = aluguelRepository.findAll(paginacao);
        return alugueis;
    }
	
	public Aluguel buscarPorId(UUID id) {
		return aluguelRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Aluguel não encontrado"));
	}
	
	@Transactional
	public void alterarStatusParaMontado(UUID id) {
	    Aluguel aluguel = aluguelRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("Aluguel não encontrado"));

	   
	    if (aluguel.getStatus() == StatusAluguel.CANCELADO) {
	        throw new NegocioException("Não é possível montar um aluguel cancelado.");
	    }

	    
	    if (aluguel.getStatus() != StatusAluguel.MONTAGEM) {
	        throw new NegocioException("O aluguel precisa estar em preparação (MONTAGEM) para ser finalizado.");
	    }

	    aluguel.setStatus(StatusAluguel.MONTADA);
	    aluguelRepository.save(aluguel);
	}
	
	
	@Transactional
	public void alterarStatusParaRetirada(UUID id) {
	    Aluguel aluguel = aluguelRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("Aluguel não encontrado"));

	    if (aluguel.getStatus() == StatusAluguel.CANCELADO) {
	        throw new NegocioException("Não é possível retirar um aluguel cancelado.");
	    }
	    if (aluguel.getStatus() != StatusAluguel.MONTADA) {
	        throw new NegocioException("O equipamento só pode ser retirado após estar com status MONTADA.");
	    }

	    aluguel.setStatus(StatusAluguel.RETIRADA);
	    aluguelRepository.save(aluguel);
	}
	
	@Transactional
	public void darEntrada(ChecklistRequest request) {
		
		Aluguel aluguel = aluguelRepository.findById(request.idAluguel())
		        .orElseThrow(() -> new EntityNotFoundException("Aluguel não encontrado"));
		
		if(aluguel.getStatus() != StatusAluguel.MONTADA ) {
			throw new NegocioException("Não é possível realizar a entrada: o aluguel deve estar no status MONTADA.");
		};
		
		aluguel.setChecklistEntrada(checklistService.entrada(request));
		
		aluguelRepository.save(aluguel);
	
	}
	
	@Transactional
	public Aluguel saida(ChecklistRequest request) {
		
		Aluguel aluguel = aluguelRepository.findById(request.idAluguel())
	            .orElseThrow(() -> new EntityNotFoundException("Aluguel não encontrado"));
		

		if (aluguel.getStatus() != StatusAluguel.RETIRADA) {
	        throw new NegocioException("Não é possível realizar a saída: o aluguel deve estar no status RETIRADA.");
	    }
		
		aluguel.setChecklistSaida(checklistService.saida(request));
		aluguel.setStatus(StatusAluguel.FINALIZADO);
		
		ItemPedido item = aluguel.getItemPedido();
	    item.setStatus(StatusItemPedido.CONCLUIDO);
	    
		
		return aluguelRepository.save(aluguel);
	
	}
	

}
