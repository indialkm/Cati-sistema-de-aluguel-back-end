package com.cati.tcc.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.cati.tcc.model.enums.TipoCheckList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name="tb_checklist")
public class Checklist {
	
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
	
	private String observacoes;
	
	
	@OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Midia> midias = new ArrayList<>();
	
	private String urlContrato;
	
	private TipoCheckList tipoChecklist; 
	
	private LocalDateTime dataRegistro = LocalDateTime.now();
	
	private boolean aprovacao;
	
	
}

