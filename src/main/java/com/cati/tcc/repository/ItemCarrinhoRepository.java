package com.cati.tcc.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cati.tcc.model.ItemCarrinho;



@Repository
public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, UUID>  {
	
	List<ItemCarrinho> findByCarrinhoId(UUID carrinhoId);

}
