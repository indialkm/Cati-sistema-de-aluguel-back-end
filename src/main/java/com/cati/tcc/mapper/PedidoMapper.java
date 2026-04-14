package com.cati.tcc.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.PedidoRequest;
import com.cati.tcc.dto.response.ItemPedidoResponse;
import com.cati.tcc.dto.response.PedidoResponse;
import com.cati.tcc.model.Carrinho;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.enums.StatusPedido;


@Component
public class PedidoMapper {

    private final ItemPedidoMapper itemPedidoMapper;
    public PedidoMapper(ItemPedidoMapper itemPedidoMapper) {
        this.itemPedidoMapper = itemPedidoMapper;
    }

    public PedidoResponse toResponse(Pedido pedido) {
       
        List<ItemPedidoResponse> itensResponse = pedido.getItensPedidos().stream()
                .map(itemPedidoMapper::toResponse)
                .toList();

        return new PedidoResponse(
            pedido.getId(),
            pedido.getValorTotal(),
            pedido.getStatus(),
            pedido.getDataPedido(),
            itensResponse 
        );
    }
    
    public Pedido toEntity(PedidoRequest request, Carrinho carrinho, Double valorTotalCalculado) {
       
    	Pedido pedido = new Pedido();
        
        pedido.setUser(carrinho.getUser());
        
        pedido.setValorTotal(valorTotalCalculado);
 
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

        pedido.setDataPedido(LocalDateTime.now());
        
        return pedido;
    }
}
