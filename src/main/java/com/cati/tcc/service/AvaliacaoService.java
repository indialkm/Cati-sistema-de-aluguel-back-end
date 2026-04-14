package com.cati.tcc.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cati.tcc.dto.request.AvaliacaoRequest;
import com.cati.tcc.mapper.AvaliacaoMapper;
import com.cati.tcc.model.Avaliacao;
import com.cati.tcc.model.Estoque;
import com.cati.tcc.model.User;
import com.cati.tcc.repository.AvaliacaoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AvaliacaoService {
	
	private final AvaliacaoMapper avaliacaoMapper;
	private final AvaliacaoRepository avaliacaoRepository;
	private final AuthService auth;
	private final UserService userService;
	private final EstoqueService estoqueService;
	private final PedidoService pedidoService;
	


	public AvaliacaoService(AvaliacaoMapper avaliacaoMapper, AvaliacaoRepository avaliacaoRepository, AuthService auth,
			UserService userService, EstoqueService estoqueService, PedidoService pedidoService) {
		this.avaliacaoMapper = avaliacaoMapper;
		this.avaliacaoRepository = avaliacaoRepository;
		this.auth = auth;
		this.userService = userService;
		this.estoqueService = estoqueService;
		this.pedidoService = pedidoService;
	}

	@Transactional
	public Avaliacao criar(UUID idEstoque, AvaliacaoRequest request) {
		
		UUID idUser = auth.getAuthenticatedUserId();
		User user = userService.buscarId(idUser);
		
		Estoque estoque = estoqueService.buscarId(idEstoque);
		
		Avaliacao avaliacao = avaliacaoMapper.toRequest(request);
		
		avaliacao.setUsuario(user);
		avaliacao.setEstoque(estoque);
		
		return avaliacaoRepository.save(avaliacao);
		
	}
	
	public Page<Avaliacao> exibirAvaliacoesEstoque(UUID idEstoque, Pageable pageable){
		
		if (estoqueService.buscarId(idEstoque) == null) {
	        throw new EntityNotFoundException("Estoque não encontrado");
	    }
		
		return avaliacaoRepository.findByEstoque_Id(idEstoque, pageable);
		
	}
	
	public Avaliacao atualizarParcial(Long id, AvaliacaoRequest dto) {
	    
	    Avaliacao avaliacaoExistente = avaliacaoRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("Avaliação não encontrada"));

	    
	    if (dto.nota() != null) {
	        avaliacaoExistente.setNota(dto.nota());
	    }
	    if (dto.comentario() != null) {
	        avaliacaoExistente.setComentario(dto.comentario());
	    }

	    
	    return avaliacaoRepository.save(avaliacaoExistente);
	}
	
	
	public void delete(Long idAvaliacao) {
		
		Avaliacao avaliacao = avaliacaoRepository.findById(idAvaliacao)
								.orElseThrow(() -> new EntityNotFoundException("Avaliacao nao encontrada"));
		
		avaliacaoRepository.delete(avaliacao);
	}
	
	
	public Double avaliacaoTotal(UUID idEstoque) {
	    Double total = avaliacaoRepository.getMediaDasNotas(idEstoque);
	    return (total != null) ? total : 0.0; 
	}
	

}
