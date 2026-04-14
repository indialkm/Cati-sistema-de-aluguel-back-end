package com.cati.tcc.mapper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.ReservaRequest;
import com.cati.tcc.dto.response.ReservaResponse;
import com.cati.tcc.model.Reserva;



@Component
public class ReservaMapper {
	
	public Reserva toEntity(ReservaRequest request) {
        if (request == null) return null;

        Reserva reserva = new Reserva();
        reserva.setDataInicial(request.dataInicial());
        reserva.setDataFinal(request.dataFinal());
        
        return reserva;
    }
	
	public ReservaResponse toResponse(Reserva reserva) {
	    if (reserva == null) return null;

	    UUID estoqueId = (reserva.getEstoque() != null) 
	                         ? reserva.getEstoque().getId() 
	                         : null;

	    return new ReservaResponse(
	        reserva.getId(),
	        reserva.getDataInicial(),
	        reserva.getDataFinal(),
	        reserva.getDisponibilidade(),
	        estoqueId
	    );
	}
	
}
