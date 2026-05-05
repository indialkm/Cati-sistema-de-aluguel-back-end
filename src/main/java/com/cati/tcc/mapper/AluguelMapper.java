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
            a.getEstoque().getNome(),
            a.getPreco(),
            a.getStatus().name(),
            reservaMapper.toResponse(a.getReserva()),
            enderecoMapper.toResponse(a.getEndereco())
        );
    }
	
	public AluguelResponseCheck toResponseComCheck(Aluguel a) {
        return new AluguelResponseCheck(
            a.getId(),
            a.getEstoque().getNome(),
            a.getPreco(),
            a.getStatus(),
            reservaMapper.toResponse(a.getReserva()),
            enderecoMapper.toResponse(a.getEndereco()),
            checklistMapper.toResponse(a.getChecklistEntrada()),
            checklistMapper.toResponse(a.getChecklistSaida())
        );
    }
	

}
