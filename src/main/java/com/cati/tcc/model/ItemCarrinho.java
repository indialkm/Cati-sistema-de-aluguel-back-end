package com.cati.tcc.model;

import java.util.List;
import java.util.UUID;

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


@Getter @Setter @NoArgsConstructor
@Entity
@Table(name="tb_itemCarrinho")
public class ItemCarrinho {
	
	@Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco enderecoEntrega; 
    
    @ManyToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva; 
    
    @ManyToOne
    @JoinColumn(name = "estoque_id")
    private Estoque estoque;
    
    @ManyToOne
    @JoinColumn(name = "carrinho_id")
    private Carrinho carrinho;
    
    private Double preco;


    
    public Double getSubtotal() {
        if (this.estoque == null || this.reserva == null) {
            return 0.0;
        }
        
        long dias = java.time.temporal.ChronoUnit.DAYS.between(
            reserva.getDataInicial(), 
            reserva.getDataFinal()
        );
        
        if (dias <= 0) dias = 1; 
        return estoque.getPrecoBase() * dias;
    }

}
