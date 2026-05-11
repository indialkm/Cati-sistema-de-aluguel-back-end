package com.cati.tcc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.cati.tcc.model.enums.TipoEstoque;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "estoque_id")
    private List<Midia> fotosModelos = new ArrayList<>();
	
	@OneToMany(mappedBy = "estoque", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Equipamento> equipamentos = new ArrayList<>();
	
	@Enumerated(EnumType.STRING)
	private TipoEstoque tipoEstoque;
	
	public void adicionarEquipamento(Equipamento equipamento){		
	    equipamento.setEstoque(this);
	    this.equipamentos.add(equipamento);
	}
	
	public void adicionarMidia(Midia midia) {
        this.fotosModelos.add(midia);
        midia.setEstoque(this);
    }
	
	
}
