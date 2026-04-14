package com.cati.tcc.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ChecklistResponse(
	
		UUID id,
		String observacoes,
		List<MidiaResponse> midias,
		LocalDateTime dataRegistro
		
		) {


}
