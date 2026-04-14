package com.cati.tcc.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cati.tcc.model.enums.StatusReserva;



public record ReservaResponse(
		
		UUID id,
		LocalDateTime dataInicial,
		LocalDateTime dataFinal,
		StatusReserva disponibilidade,
		UUID idEquipamento
		
		) {}
