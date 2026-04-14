package com.cati.tcc.mapper;

import java.util.HashSet;
import org.springframework.stereotype.Component;
import com.cati.tcc.dto.request.UserRequest;
import com.cati.tcc.dto.response.UserResponse;
import com.cati.tcc.model.User;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User();
        
        user.setNome(request.nome());
        user.setEmail(request.email());
        user.setCpf(request.cpf());
        user.setCnpj(request.cnpj());
        user.setTelefone(request.telefone());
       
        user.setPassword(request.password());
        
        user.setRoles(request.roles() != null ? new HashSet<>(request.roles()) : new HashSet<>());

        return user;
    }

    public UserResponse toResponse(User user) {
        if (user == null) return null;

        return new UserResponse(
            user.getId(),
            user.getNome(),
            user.getEmail(),
            user.getTelefone(),
            user.getRoles() 
        );
    }
}