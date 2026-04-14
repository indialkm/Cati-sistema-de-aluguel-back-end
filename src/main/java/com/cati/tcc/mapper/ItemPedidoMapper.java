package com.cati.tcc.mapper;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.ItemPedidoRequest;
import com.cati.tcc.dto.response.ItemPedidoResponse;
import com.cati.tcc.model.ItemCarrinho;
import com.cati.tcc.model.ItemPedido;


@Component
public class ItemPedidoMapper {

	public ItemPedidoResponse toResponse(ItemPedido item) {
	    return new ItemPedidoResponse(
	        
	    	item.getId(),                               
	    	item.getEstoque().getId(),
	    	item.getEstoque().getNome(),
	        item.getReserva().getId(),                  
	        item.getPreco(),                     
	        item.getEnderecoEntrega().getId()         
	       
	    );
	}


    public ItemPedido toEntity(ItemPedidoRequest request) {
        ItemPedido item = new ItemPedido();
        return item;
    }
    
    public ItemPedido toPedido(ItemCarrinho itemCarrinho) {
    	
    	ItemPedido pedido = new ItemPedido();
    	
    	pedido.setEstoque(itemCarrinho.getEstoque());
    	pedido.setReserva(itemCarrinho.getReserva());
    	pedido.setEnderecoEntrega(itemCarrinho.getEnderecoEntrega());
    	pedido.setPreco(itemCarrinho.getPreco());
    	
    	return pedido;
    	
    }
}