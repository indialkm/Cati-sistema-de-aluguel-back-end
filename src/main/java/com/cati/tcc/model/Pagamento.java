package com.cati.tcc.model;


import java.time.LocalDateTime;
import java.util.UUID;

import com.cati.tcc.model.enums.FormasPagamento;
import com.cati.tcc.model.enums.StatusPagamento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "tb_pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @OneToOne
    @JoinColumn(name = "pedido_id", referencedColumnName = "id")
    private Pedido pedido; 

    @Column(name = "valor_pago", nullable = false, updatable = false)
    private Double valorPago;
    
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    private StatusPagamento status;
    
    @Enumerated(EnumType.STRING)
    private FormasPagamento formaPagamento; 
    
    private String transacaoGatewayId;
    
    @Column(columnDefinition = "TEXT")
    private String payload_retorno;
    
    private Integer parcelas;
    
}
