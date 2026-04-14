package com.cati.tcc.mapper;


import java.util.List;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.CarrinhoRequest;
import com.cati.tcc.dto.response.CarrinhoResponse;
import com.cati.tcc.dto.response.ItemCarrinhoResponse;
import com.cati.tcc.model.Carrinho;
import com.cati.tcc.model.enums.StatusCarrinho;



@Component
public class CarrinhoMapper {
	
	private final ItemCarrinhoMapper itemMapper;

    public CarrinhoMapper(ItemCarrinhoMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public Carrinho toEntity(CarrinhoRequest request) {
        if (request == null) return null;

        Carrinho carrinho = new Carrinho();
     

        carrinho.setStatusCarrinho(StatusCarrinho.ABERTO); 
        
        return carrinho;
    }
    
    public CarrinhoResponse toResponse(Carrinho entity) {
        if (entity == null) return null;

            List<ItemCarrinhoResponse> itensResponse = entity.getItens() != null 
            ? entity.getItens().stream().map(itemMapper::toResponse).toList()
            : List.of();

        return new CarrinhoResponse(
            entity.getId(),
            entity.getUser() != null ? entity.getUser().getId() : null,
            entity.getTotal(),
            entity.getDataCriacao(),
            entity.getStatusCarrinho(),
            itensResponse
        );
    }
	
	

}
