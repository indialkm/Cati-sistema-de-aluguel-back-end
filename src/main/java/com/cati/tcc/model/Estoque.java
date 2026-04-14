package com.cati.tcc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter @NoArgsConstructor
@Table(name="tb_estoque")
public class Estoque {
	
	@Id
	@GeneratedValue(strategy= GenerationType.UUID)
	private UUID id;
	
	private int quantidade;
	private String descricao;
	private String nome;
	private Double precoBase;
	private String categoria;
	private Double altura;
	private Double largura;
	
	
	@ElementCollection
	private List<String> fotosModelos;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_id")
	private List<Equipamento> equipamentos = new ArrayList<>();;
	
	public void adicionarEquipamento(Equipamento equipamento){		
		this.equipamentos.add(equipamento);

	}
	

}
