package com.cati.tcc.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cati.tcc.config.exceptions.ResourceNotFoundException;
import com.cati.tcc.dto.request.EquipamentoRequest;
import com.cati.tcc.dto.request.EstoqueRequest;
import com.cati.tcc.dto.response.DetalhesModeloResponse;
import com.cati.tcc.dto.response.EstoqueResponse;
import com.cati.tcc.dto.update.EstoquePatchDTO;
import com.cati.tcc.mapper.EstoqueMapper;
import com.cati.tcc.model.Equipamento;
import com.cati.tcc.model.Estoque;
import com.cati.tcc.model.Midia;
import com.cati.tcc.model.enums.StatusEquipamento;
import com.cati.tcc.repository.EstoqueRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class EstoqueService {
	
	private final EstoqueRepository estoqueRepository;
    private final EstoqueMapper estoqueMapper;
    private final MidiaService midiaService;

	public EstoqueService(EstoqueRepository estoqueRepository, EstoqueMapper estoqueMapper, MidiaService midiaService) {
		this.estoqueRepository = estoqueRepository;
		this.estoqueMapper = estoqueMapper;
		this.midiaService = midiaService;
	}

	@Transactional
	public Estoque criar(EstoqueRequest request, List<MultipartFile> arquivos) {
	    
	    if(estoqueRepository.existsByNome(request.nome())){
	        throw new IllegalArgumentException("Já existe um modelo de estoque cadastrado com este nome.");
	    }
	    Estoque estoque = estoqueMapper.toEntity(request);
	    if (arquivos != null && !arquivos.isEmpty()) {
	        List<Midia> midias = midiaService.salvarMidiasLocais(arquivos);
	        
	        for(Midia m : midias) {
	        	estoque.adicionarMidia(m);
	        	
	          }
	        }
 
	    int quantidadeEquipamentosEmEstoque = (estoque.getEquipamentos() != null)
	    ? estoque.getEquipamentos().size(): 0;
	    
	    estoque.setQuantidade(quantidadeEquipamentosEmEstoque);
	    return estoqueRepository.save(estoque);
	}
	
	
	
	@Transactional
	public Estoque atualizarParcial(UUID id, EstoquePatchDTO dto) {
	    Estoque estoqueExistente = buscarPorId(id);

	    dto.getNome().ifPresent(estoqueExistente::setNome);
	    dto.getDescricao().ifPresent(estoqueExistente::setDescricao);
	    dto.getPrecoBase().ifPresent(estoqueExistente::setPrecoBase);
	    dto.getCategoria().ifPresent(estoqueExistente::setCategoria);
	    dto.getAltura().ifPresent(estoqueExistente::setAltura);
	    dto.getLargura().ifPresent(estoqueExistente::setLargura);
	    dto.getTipoEstoque().ifPresent(estoqueExistente::setTipoEstoque);

	    return estoqueRepository.save(estoqueExistente);
	}
	
	public Estoque buscarId(UUID id) {
		return estoqueRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Estoque não encontrado"));
	}
	
	public List<Estoque> verificarDisponibilidadeParaReserva(UUID id, LocalDateTime inicio, LocalDateTime fim) {
        return estoqueRepository.buscarEstoquesDisponiveis(id, inicio, fim);
    }

	public Page<Estoque> listarEstoques(Pageable pageable){

		return estoqueRepository.findAll(pageable);
		}
	
	public Estoque atualizar(Estoque estoque) {
		return estoqueRepository.save(estoque);
	}
	
	@Transactional
	public void baixarEstoque(UUID estoqueId, int quantidade) {
	    Estoque estoque = buscarId(estoqueId);
	    if (estoque.getQuantidade() < quantidade) {
	        throw new IllegalArgumentException("Estoque insuficiente para o item: " + estoque.getNome());
	    }
	    estoque.setQuantidade(estoque.getQuantidade() - quantidade);
	    estoqueRepository.save(estoque);
	}

	@Transactional
	public void retornarEstoque(UUID estoqueId, int quantidade) {
	    Estoque estoque = buscarId(estoqueId);
	    estoque.setQuantidade(estoque.getQuantidade() + quantidade);
	    estoqueRepository.save(estoque);
	}
	
	public long contarItensOperacionais(UUID estoqueId) {
	    Estoque estoque = estoqueRepository.findById(estoqueId)
	        .orElseThrow(() -> new EntityNotFoundException("Estoque não encontrado"));

	    return estoque.getEquipamentos().stream()
	        .filter(eq -> eq.getStatusEquipamento() == StatusEquipamento.DISPONIVEL)
	        .count();
	}
	
	public Estoque buscarPorId(UUID id) {
        return estoqueRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estoque não encontrado"));
    }
	
	public boolean estaDisponivel(Estoque estoque)
	{
		return estoque.getEquipamentos().stream()
				.anyMatch(equipamento -> equipamento.getStatusEquipamento() == StatusEquipamento.DISPONIVEL);
	}
	
	
	public DetalhesModeloResponse buscarDetalhesProcessados(UUID id) {
       
        Estoque estoque = estoqueRepository.findByIdWithEquipamentos(id)
            .orElseThrow(() -> new ResourceNotFoundException("Estoque não localizado"));

        Double precoFinal = estoque.getPrecoBase();
        boolean estaDisponivel = estaDisponivel(estoque);

        return estoqueMapper.toDetalhesResponse(estoque, precoFinal, estaDisponivel);
    }
	
	public List<Estoque> filtrarEstoque(String categoria, String modelo, Double largura, Integer qtdMinima) {
	    
	    String catFiltro = (categoria != null && !categoria.isBlank()) ? categoria : null;
	    String modFiltro = (modelo != null && !modelo.isBlank()) ? modelo : null;
	    
	    Double largFiltro = (largura != null && largura > 0) ? largura : null;
	    
	    int minima = (qtdMinima != null) ? qtdMinima : 0;
	    
	    return estoqueRepository.buscarComFiltrosOpcionais(catFiltro, modFiltro, largFiltro, minima);
	}
	
	
	public Page<Estoque> filtrarPorCategoria(String nomeCategoria, Pageable pageable) {
	    return estoqueRepository.findByCategoriaContainingIgnoreCase(nomeCategoria, pageable);
	}
	
	@Transactional
    public Page<Estoque> listarTodos(Pageable pageable) {
        return estoqueRepository.findAll(pageable);
    }
	
	@Transactional 
	public void excluir(UUID id) {
	    
	    Estoque estoque = estoqueRepository.findById(id)
	        .orElseThrow(() -> new EntityNotFoundException("Estoque não encontrado"));

	    if (estoque.getFotosModelos() != null) {
	        estoque.getFotosModelos().clear();
	    }
	    
	    if (estoque.getEquipamentos() != null) {
	        estoque.getEquipamentos().clear();
	    }
	    estoqueRepository.saveAndFlush(estoque);
	    estoqueRepository.delete(estoque);
	}
	
	
}


	