package com.cati.tcc.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.cati.tcc.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name="tb_user")
public class User {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@NotBlank(message = "Nome não pode ser vazio")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 letras")
	@Column(nullable= false, length = 100)
	private String nome;
	
	@JsonIgnore
	@NotBlank(message = "Senha é obrigatória")
	@Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
	@Column(nullable = false)
	private String password;
	
	//@CPF(message = "CPF inválido")
	@Column(length = 11, nullable = false, unique = true)
	private String cpf;
	
	private String razaoSocial;
	
	//@CNPJ(message = "CNPJ inválido")
	@Column(length = 14, nullable = true, unique = true)
	private String cnpj;

	@NotBlank(message = "Telefone é obrigatório")
	@Pattern(regexp = "^\\d{10,11}$", message = "Telefone deve ter 10 ou 11 dígitos (apenas números)")
	@Column(length = 15, nullable = false)
	private String telefone;
	
	@Email(message = "E-mail inválido")
    @NotBlank
	@Column(name = "email", length = 255, unique = true, nullable = false)
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Set<Role> roles = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private List<Endereco> enderecos = new ArrayList<>();
	
	public void adicionarEndereco(Endereco endereco) {
	    this.enderecos.add(endereco); 
	}
	
}

