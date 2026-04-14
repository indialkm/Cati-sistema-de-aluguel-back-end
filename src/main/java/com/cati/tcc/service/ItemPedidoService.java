package com.cati.tcc.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cati.tcc.mapper.ItemPedidoMapper;
import com.cati.tcc.model.ItemCarrinho;
import com.cati.tcc.model.ItemPedido;
import com.cati.tcc.model.Pedido;
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
}
