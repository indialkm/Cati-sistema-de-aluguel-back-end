package com.cati.tcc.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.cati.tcc.model.enums.StatusCarrinho;
import com.cati.tcc.model.enums.StatusPagamento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter @NoArgsConstructor
@Entity
@Table(name="historicoTransacao")
public class HistoricoTransacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @ManyToOne
    @JsonIgnoreProperties({"senha", "roles", "pedidos", "enderecos"})
    private User user;

    @ManyToOne
    @JsonIgnoreProperties({"pedido", "historicos"})
    private Pagamento pagamento;
    
    @ManyToOne
    @JsonIgnoreProperties({"itensPedidos", "user", "pagamento"})
    private Pedido pedido;
    
    @Enumerated(EnumType.STRING)
    private StatusPagamento status;
    
    private LocalDateTime dataEvento;
    
    private String descricao;
}