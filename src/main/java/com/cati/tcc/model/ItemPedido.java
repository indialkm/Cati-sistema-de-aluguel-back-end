package com.cati.tcc.model;

import java.util.UUID;

import com.cati.tcc.model.enums.StatusItemPedido;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "tb_itemPedido")
public class ItemPedido {
	
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

	@JsonBackReference
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    private Estoque estoque;

    @ManyToOne
    private Reserva reserva;

    @ManyToOne
    private Endereco enderecoEntrega;

    private Double preco;
  
    private StatusItemPedido status = StatusItemPedido.AGUARDANDO;


}
