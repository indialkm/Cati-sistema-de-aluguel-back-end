package com.cati.tcc.mapper;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.ItemCarrinhoRequest;
import com.cati.tcc.dto.response.ItemCarrinhoResponse;
import com.cati.tcc.model.ItemCarrinho;
import com.cati.tcc.model.ItemPedido;


@Component
public class ItemCarrinhoMapper {
	
	public ItemCarrinho toEntity(ItemCarrinhoRequest request) {
        if (request == null) return null;
        
        ItemCarrinho item = new ItemCarrinho();
        
        return item;
    }

    public ItemCarrinhoResponse toResponse(ItemCarrinho item) {
        if (item == null) return null;

        return new ItemCarrinhoResponse(
            item.getId(),
            item.getEnderecoEntrega(),
            item.getReserva().getId(),
            item.getReserva().getDataInicial(),
            item.getReserva().getDataFinal(),
            item.getEstoque().getId(),
            item.getEstoque().getNome(),
            item.getEstoque().getPrecoBase()
        );
    }
    
  

}
