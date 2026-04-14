package com.cati.tcc.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import com.cati.tcc.model.enums.StatusAluguel;

import jakarta.persistence.CascadeType;
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
@Table(name="tb_aluguel")
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne
    @JoinColumn(name = "item_pedido_id")
    private ItemPedido itemPedido;

    @OneToOne(cascade = CascadeType.ALL)
    private Checklist checklistEntrada;

    @OneToOne(cascade = CascadeType.ALL)
    private Checklist checklistSaida;

    @Enumerated(EnumType.STRING)
    private StatusAluguel status = StatusAluguel.AGUARDANDO;

}

