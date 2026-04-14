package com.cati.tcc.mapper;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.MidiaRequest;
import com.cati.tcc.dto.response.MidiaResponse;
import com.cati.tcc.model.Midia;

@Component
public class MidiaMapper {
	
	public MidiaResponse toResponse(Midia midia) {
		
		return new MidiaResponse(
			
				midia.getId(),
				midia.getUrl(),
				midia.getNomeArquivo(),
				midia.getTipo()
				
				);
	}
	
	
	public Midia toEntity(MidiaRequest request) {
		
	Midia midia = new Midia();
	
	midia.setUrl(request.url());
	midia.setNomeArquivo(request.nomeArquivo());
	
	return midia;
				
		
	}

}
