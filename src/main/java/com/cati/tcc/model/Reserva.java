package com.cati.tcc.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.cati.tcc.model.enums.StatusReserva;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	
	@OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemCarrinho> itensCarrinho;
	
	@OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemPedido> itensPedido;
	
	@ManyToOne
    @JoinColumn(name = "estoque_id") 
    private Estoque estoque;
	
	private LocalDateTime auditoriaReserva;
	
	public Long getQuantidadeDiasAluguel() {
	   
	    if (this.dataInicial == null || this.dataFinal == null) {
	        return 0L;
	    }
	    
	    long dias = ChronoUnit.DAYS.between(this.dataInicial, this.dataFinal);
	    
	    return Math.max(1L, dias);
	}

}
