package com.cati.tcc.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.cati.tcc.model.enums.TipoCheckList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
	
	@NotEmpty(message = "A lista de mídias não pode estar vazia")
    @Size(min = 1, message = "É necessário enviar pelo menos uma foto ou vídeo")
	@OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Midia> midias = new ArrayList<>();
	
	private String urlContrato;
	
	@ManyToOne
	@JoinColumn(name = "aluguel_id", nullable = false)
	private Aluguel aluguel;
	
	@Enumerated(EnumType.STRING)
	private TipoCheckList tipoChecklist; 
	
	private LocalDateTime dataRegistro = LocalDateTime.now();
	
	private boolean aprovacao;
	
	public void adicionarMidia(Midia midia) {
	    this.midias.add(midia);
	    midia.setChecklist(this);
	}
}

