package com.cati.tcc.model;


import java.time.LocalDateTime;
import java.util.UUID;

import com.cati.tcc.model.enums.NivelDeConservacao;
import com.cati.tcc.model.enums.StatusEquipamento;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name="tb_equipamento")
public class Equipamento {
	
	@Id
	@GeneratedValue(strategy= GenerationType.UUID)
	private UUID id;
	
	private String numeroSerie;
	private String modelo;
	private String cor;
	private String observacoesInternas;
	private Double altura;
	private Double largura;
	private Double peso;
	private Double preco;
	private LocalDateTime dataCriacao;
	private NivelDeConservacao condicao;
	private StatusEquipamento statusEquipamento;
	
	
}

