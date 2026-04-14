package com.cati.tcc.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cati.tcc.dto.request.ItemCarrinhoRequest;
import com.cati.tcc.mapper.ItemCarrinhoMapper;
import com.cati.tcc.model.Carrinho;
import com.cati.tcc.model.Endereco;
import com.cati.tcc.model.Estoque;
import com.cati.tcc.model.ItemCarrinho;
import com.cati.tcc.model.Reserva;
import com.cati.tcc.repository.ItemCarrinhoRepository;

import java.util.List;

import jakarta.transaction.Transactional;

@Service
public class ItemCarrinhoService {
	
	private final ItemCarrinhoRepository repository;
	private final ItemCarrinhoMapper mapper;
	
	
	//Services
	private final ReservaService reservaService;
	private final EstoqueService estoqueService;
	private final EnderecoService enderecoService;
	

	// Criar ItemCarrinho

	public ItemCarrinhoService(ItemCarrinhoRepository repository, ItemCarrinhoMapper mapper,
			 ReservaService reservaService, EstoqueService estoqueService,
			EnderecoService enderecoService) {

		this.repository = repository;
		this.mapper = mapper;
		this.reservaService = reservaService;
		this.estoqueService = estoqueService;
		this.enderecoService = enderecoService;
	}

	/**
	 * Fluxo de Aluguel e Pagamento
	 * 
	 * 2° Aqui o itemCarrinho nasce e é populado dentro do carrinho.
	 * Para isso nós criamos as classes que irão receber todas as informações relevantes.

	 * @param UUID idCarrinho
	 * @param ItemCarrinhoRequest request
	 * 
	 * @returns itemCarrinho
	 * 
	 * */ 
	@Transactional
	public ItemCarrinho criar(Carrinho carrinho, ItemCarrinhoRequest request) {
	    
	    // 1. Busca as dependências (Estoque, Reserva, Endereço)
	    // Dica: Use os métodos que já lançam exceção se não encontrar
	    Reserva reserva = reservaService.buscarPorId(request.idReserva());
	    Endereco endereco = enderecoService.buscarId(request.idEndereco());
	    Estoque estoque = reserva.getEstoque();
	           
	    // 2. Cria a entidade ItemCarrinho
	    ItemCarrinho item = new ItemCarrinho(); 
	    item.setCarrinho(carrinho); 
	    item.setReserva(reserva);
	    item.setEstoque(estoque);
	    item.setEnderecoEntrega(endereco);
	   
	    Double valorMultiplicadosPorDias =  item.getSubtotal();
	    
	    item.setPreco(valorMultiplicadosPorDias);
	    
	    if (carrinho.getItens() != null) {
	        carrinho.getItens().add(item);
	    }
	    // 4. Salva e retorna
	    return repository.save(item);
	}


	
	
	
	

}
