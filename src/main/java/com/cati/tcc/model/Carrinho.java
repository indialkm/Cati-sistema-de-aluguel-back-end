package com.cati.tcc.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.cati.tcc.model.enums.StatusCarrinho;

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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name="tb_carrinho")
public class Carrinho {
	
	@Id
	@GeneratedValue(strategy= GenerationType.UUID)
	private UUID id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@OneToMany(mappedBy = "carrinho", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemCarrinho> itens = new ArrayList<>();
	private Double total = 0.0;
	private LocalDateTime dataCriacao = LocalDateTime.now();
	
	@Enumerated(EnumType.STRING)
	StatusCarrinho statusCarrinho;
	
	// Dentro de Carrinho.java
	public void atualizarTotal() {
	    this.total = (itens == null) ? 0.0 : 
	        itens.stream()
	             .mapToDouble(ItemCarrinho::getSubtotal)
	             .sum();
	}
	
	public void setItens(ItemCarrinho item) {
		itens.add(item);

	}

}
