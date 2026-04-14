package com.cati.tcc.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cati.tcc.dto.request.ChecklistRequest;
import com.cati.tcc.model.Aluguel;
import com.cati.tcc.model.ItemPedido;
import com.cati.tcc.model.NotaPedido;
import com.cati.tcc.model.Pedido;

import jakarta.transaction.Transactional;

@Service
public class CheckoutService {

	private final PedidoService pedidoService;
	private final AluguelService aluguelService;
	private final ItemPedidoService itemPedido;
	private final UserService userService;
	private final AuthService auth;
	

	public CheckoutService(PedidoService pedidoService, AluguelService aluguelService, ItemPedidoService itemPedido,
			 UserService userService, AuthService auth) {
		this.pedidoService = pedidoService;
		this.aluguelService = aluguelService;
		this.itemPedido = itemPedido;
		this.userService = userService;
		this.auth = auth;
	}



	@Transactional
	public Pedido checkout(ChecklistRequest request) {
		
		UUID userId = auth.getAuthenticatedUserId();
		
		//fechar o aluguel
		Aluguel aluguel = aluguelService.saida(request);
		
		//pega o itempedido
		ItemPedido item = aluguel.getItemPedido();			
		
		//fecha o pedido
		Pedido pedido = pedidoService.fecharPedido(item);
		
		return pedido;
		
	}
				
				
	}		
	
	

