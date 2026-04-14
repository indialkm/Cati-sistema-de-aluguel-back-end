package com.cati.tcc.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.cati.tcc.model.enums.StatusReserva;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter @NoArgsConstructor
@Table(name="tb_reserva")
public class Reserva {
	
	@Id
	@GeneratedValue(strategy= GenerationType.UUID)
	private UUID id;
	
	private LocalDateTime dataInicial;
	private LocalDateTime dataFinal;
	private StatusReserva disponibilidade;
	
	@ManyToOne
    @JoinColumn(name = "estoque_id") 
    private Estoque estoque;

}
