package com.cati.tcc.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cati.tcc.config.exceptions.NegocioException;
import com.cati.tcc.dto.request.ChecklistRequest;
import com.cati.tcc.model.Aluguel;
import com.cati.tcc.model.Estoque;
import com.cati.tcc.model.ItemPedido;
import com.cati.tcc.model.NotaPedido;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.enums.StatusAluguel;
import com.cati.tcc.model.enums.StatusPedido;

import jakarta.transaction.Transactional;

@Service
public class CheckoutService {

	private final PedidoService pedidoService;
	private final AluguelService aluguelService;
	private final ItemPedidoService itemPedido;
	private final UserService userService;
	private final AuthService auth;
	private final EstoqueService estoqueService;
	

	public CheckoutService(PedidoService pedidoService, AluguelService aluguelService, ItemPedidoService itemPedido,
			UserService userService, AuthService auth, EstoqueService estoqueService) {
		this.pedidoService = pedidoService;
		this.aluguelService = aluguelService;
		this.itemPedido = itemPedido;
		this.userService = userService;
		this.auth = auth;
		this.estoqueService = estoqueService;
	}

	
	@Transactional
	public Boolean checkout(UUID idPedido) {
		
		
		UUID userId = auth.getAuthenticatedUserId();
		
		boolean alugueisTodosFinalizados = aluguelService.fecharAluguel(idPedido);
		
		if (!alugueisTodosFinalizados) { 
		    throw new NegocioException("Não é possível fazer o checkout porque ainda tem itens a serem concluídos no pedido");
		}
		
		pedidoService.atualizarStatus(idPedido, StatusPedido.FINALIZADO);
	
		return true;
		
	
		}		
				
	}		
	
	

