package com.cati.tcc.mapper;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.response.AluguelResponse;
import com.cati.tcc.dto.response.AluguelResponseCheck;
import com.cati.tcc.dto.response.ReservaResponse;
import com.cati.tcc.model.Aluguel;

@Component
public class AluguelMapper {
	
	private final EnderecoMapper enderecoMapper;
    private final ChecklistMapper checklistMapper;
    private final ReservaMapper reservaMapper;

    

    public AluguelMapper(EnderecoMapper enderecoMapper, ChecklistMapper checklistMapper, ReservaMapper reservaMapper) {
		this.enderecoMapper = enderecoMapper;
		this.checklistMapper = checklistMapper;
		this.reservaMapper = reservaMapper;
	}


	public AluguelResponse toResponse(Aluguel a) {
        return new AluguelResponse(
            a.getId(),
            a.getItemPedido().getEstoque().getNome(),
            a.getItemPedido().getPreco(),
            a.getStatus(),
            reservaMapper.toResponse(a.getItemPedido().getReserva()),
            enderecoMapper.toResponse(a.getItemPedido().getEnderecoEntrega())
        );
    }
	
	public AluguelResponseCheck toResponseComCheck(Aluguel a) {
        return new AluguelResponseCheck(
            a.getId(),
            a.getItemPedido().getEstoque().getNome(),
            a.getItemPedido().getPreco(),
            a.getStatus(),
            reservaMapper.toResponse(a.getItemPedido().getReserva()),
            enderecoMapper.toResponse(a.getItemPedido().getEnderecoEntrega()),
            checklistMapper.toResponse(a.getChecklistEntrada()),
            checklistMapper.toResponse(a.getChecklistSaida())
        );
    }
	

}
