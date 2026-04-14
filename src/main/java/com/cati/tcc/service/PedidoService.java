package com.cati.tcc.service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cati.tcc.model.Carrinho;
import com.cati.tcc.model.Estoque;
import com.cati.tcc.model.ItemCarrinho;
import com.cati.tcc.model.ItemPedido;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.User;
import com.cati.tcc.model.enums.StatusItemPedido;
import com.cati.tcc.model.enums.StatusPagamento;
import com.cati.tcc.model.enums.StatusPedido;
import com.cati.tcc.repository.PedidoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {
	
	private final AuthService authService; 
    private final PedidoRepository pedidoRepository;
    

    
    private final UserService userService;
    private final ItemPedidoService itemService;
    

	public PedidoService(AuthService authService, PedidoRepository pedidoRepository, UserService userService,
			ItemPedidoService itemService) {
		this.authService = authService;
		this.pedidoRepository = pedidoRepository;
		this.userService = userService;
		this.itemService = itemService;
	}

	@Transactional
	public Pedido gerarPedidoDeCarrinho(Carrinho carrinho) {
		
		UUID userId = authService.getAuthenticatedUserId();
		
		Pedido pedido = new Pedido();
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        
        User user = userService.buscarId(userId);
        pedido.setUser(user);
        
        List<ItemCarrinho> itens = carrinho.getItens();
        
        
        List<ItemPedido> itemPedidos = itens.stream()
        	.map(i -> itemService.criarItemPedido(i))
        	.toList();
        
       pedido.setItensPedidos(itemPedidos);
    
    		   					
       pedido.setValorTotal(carrinho.getTotal());
       
       
       pedidoRepository.save(pedido);
       
       itemService.salvandoPedidoNoItemPedido(itemPedidos,pedido);
       
       return pedido;
       
	}
	
	@Transactional
	public Pedido buscarPorId(UUID id) {

		return pedidoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
		
	}
	
	@Transactional
	public Pedido atualizarStatus(UUID id, StatusPedido status) {
		
		return pedidoRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
		
	}
	
	public Pedido fecharPedido(ItemPedido item) {
		
		UUID id = item.getPedido().getId();
		
		Pedido pedido = pedidoRepository.findById(id)
						.orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado"));
		
		boolean todosConcluidos = pedido.getItensPedidos().stream()
	            .allMatch(i -> i.getStatus() == StatusItemPedido.CONCLUIDO);
		
		if (todosConcluidos) {
	        pedido.setStatus(StatusPedido.CONCLUIDO);
	        
	        return pedidoRepository.save(pedido);
	    }
	    
	    return pedido; 
	}
		
	
	

	
	/*UTILS METODOS PEDIDO*/
	

	
}
