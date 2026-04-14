package com.cati.tcc.dto.request;

import java.util.List;
import java.util.UUID;

public record ChecklistRequest(	
		
		UUID idAluguel,
		String observacoes,
		String urlContrato,
		List<MidiaRequest> midias
		
		) {

}
