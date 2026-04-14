package com.cati.tcc.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cati.tcc.config.security.UserSpringSecurity;
import com.cati.tcc.model.User;
import com.cati.tcc.repository.UserRepository;

@Service
public class UserDetailsImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    // LOG: Verificando o que o Spring Security recebeu
	    System.out.println("Tentando autenticar o usuário: " + username);

	    User user = this.userRepository.findByEmail(username)
	                .orElseThrow(() -> {
	                    // LOG: Caso o usuário não exista no banco
	                    System.out.println("ERRO: Usuário não encontrado no banco com o e-mail: " + username);
	                    return new UsernameNotFoundException("Credenciais invalidas.");
	                });

	    System.out.println("Usuário encontrado! ID: " + user.getId());
	    return new UserSpringSecurity(user.getEmail(), user.getPassword(), user.getId(), user.getRoles());
	}

}
