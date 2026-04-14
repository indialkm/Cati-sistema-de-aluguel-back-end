package com.cati.tcc.mapper;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.AvaliacaoRequest;
import com.cati.tcc.dto.response.AvaliacaoResponse;
import com.cati.tcc.model.Avaliacao;

@Component
public class AvaliacaoMapper {
	
	public Avaliacao toRequest(AvaliacaoRequest request) {
		
		Avaliacao a = new Avaliacao();
		
		a.setComentario(request.comentario());
		a.setNota(request.nota());
		
		return a;
	}
	
	public AvaliacaoResponse toResponse(Avaliacao a) {
		
		return new AvaliacaoResponse(
				
				a.getId(),
				a.getUsuario().getNome(),
				a.getComentario(),
				a.getNota()
				
				);
		
	}

}
