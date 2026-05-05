package com.cati.tcc.dto.response;

import java.time.LocalDateTime;

public record PeriodoBloqueadoResponse(
		LocalDateTime inicio,
	    LocalDateTime fim
	    ) {}
