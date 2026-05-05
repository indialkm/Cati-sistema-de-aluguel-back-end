package com.cati.tcc.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class PeriodoBloqueado {
	LocalDateTime inicio;
    LocalDateTime fim;
	
    public PeriodoBloqueado(LocalDateTime inicio, LocalDateTime fim) {
		this.inicio = inicio;
		this.fim = fim;
	}
    
    

}
