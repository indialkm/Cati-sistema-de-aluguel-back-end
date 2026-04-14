package com.cati.tcc.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cati.tcc.model.enums.StatusReserva;


public record ReservaRequest(
		
		UUID idEstoque,
		LocalDateTime dataInicial,
		LocalDateTime dataFinal
		
		) {}
