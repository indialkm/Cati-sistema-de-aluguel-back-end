package com.cati.tcc.dto.request;

import java.util.Set;

import com.cati.tcc.model.enums.Role;


public record UserRequest(
		
	    String nome,
	    String email,
	    String password,
	    String cpf,
	    String cnpj,
	    String telefone,
	    Set<Role> roles
		
		) {

}
