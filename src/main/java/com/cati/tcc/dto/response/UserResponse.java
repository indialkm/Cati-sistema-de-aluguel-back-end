package com.cati.tcc.dto.response;

import java.util.Set;
import java.util.UUID;

import com.cati.tcc.model.enums.Role;

public record UserResponse(

		UUID id,
		String nome,
		String email,
		String telefone,
		Set<Role> role
		//List<EnderecoResponse> enderecos
		
		) {

}
