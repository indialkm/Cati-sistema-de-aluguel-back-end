package com.cati.tcc.repository;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cati.tcc.model.Carrinho;
import com.cati.tcc.model.enums.StatusCarrinho;



@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, UUID> {
	
	Optional<Carrinho> findByUser(UUID idUsuario);
	Optional<Carrinho> findByUserIdAndStatusCarrinho(UUID idUsuario, StatusCarrinho aberto);

}
