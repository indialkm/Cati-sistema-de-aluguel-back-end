package com.cati.tcc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cati.tcc.mapper.ItemPedidoMapper;
import com.cati.tcc.model.ItemCarrinho;
import com.cati.tcc.model.ItemPedido;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.Reserva;
import com.cati.tcc.model.enums.StatusItemPedido;
import com.cati.tcc.model.enums.StatusPedido;
import com.cati.tcc.model.enums.StatusReserva;
import com.cati.tcc.repository.ItemPedidoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ItemPedidoService {

	private final ItemPedidoRepository itemRepository;
	private final ItemPedidoMapper itemMapper;
	
	public ItemPedidoService(ItemPedidoRepository itemRepository, ItemPedidoMapper itemMapper) {
		this.itemRepository = itemRepository;
		this.itemMapper = itemMapper;
	}
	
	@Transactional
	public ItemPedido criarItemPedido(ItemCarrinho item) {
		
		Reserva reserva = item.getReserva();
		reserva.setDisponibilidade(StatusReserva.AGUARDANDO_PAGAMENTO);
		reserva.setAuditoriaReserva(LocalDateTime.now());
		
		item.setReserva(reserva);
		
		return itemMapper.toPedido(item);
		
	}
	
	@Transactional
	public void salvandoPedidoNoItemPedido(List<ItemPedido> items, Pedido pedido) {
		
		items.stream()
			.peek(i -> i.setPedido(pedido))
			.forEach(i -> itemRepository.save(i));	
	}
	
	
	public Page<ItemPedido> buscarItensDoPedidoPaginado(UUID pedidoId, Pageable pageable) {
		
	    return itemRepository.findByPedidoId(pedidoId, pageable);
	   
	}
	
	public ItemPedido buscarPorId(UUID id) {
		
		return itemRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Esse item do pedido não foi encontrado"));
		
	}
	
	public List<ItemPedido> buscarTodos() {
		return itemRepository.findAll();
	}
	
	/*****************MUDAR STATUS*********************************/
	
	@Transactional
	public void statusConcluido(UUID idItem) {
		
		ItemPedido item = itemRepository.findById(idItem)
						.orElseThrow(() -> new EntityNotFoundException("Esse item do pedido não foi encontrado"));
		
		item.setStatus(StatusItemPedido.CONCLUIDO);	
		itemRepository.save(item);
	}
	
	
	@Transactional
	public void statusCancelado(UUID idItem) {
		
		ItemPedido item = itemRepository.findById(idItem)
						.orElseThrow(() -> new EntityNotFoundException("Esse item do pedido não foi encontrado"));
		
		item.setStatus(StatusItemPedido.CANCELADO);	
		itemRepository.save(item);
	}
	
	@Transactional
	public void statusAguardando(UUID idItem) {
		
		ItemPedido item = itemRepository.findById(idItem)
						.orElseThrow(() -> new EntityNotFoundException("Esse item do pedido não foi encontrado"));
		
		item.setStatus(StatusItemPedido.AGUARDANDO);	
		itemRepository.save(item);
		

	}
	
	/*****************MUDAR STATUS*********************************/
}
