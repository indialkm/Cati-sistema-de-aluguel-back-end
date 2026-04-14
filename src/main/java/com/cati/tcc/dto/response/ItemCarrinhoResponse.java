package com.cati.tcc.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.cati.tcc.model.Endereco;



public record ItemCarrinhoResponse(
		
		UUID id,
	    Endereco endereco,
	    
	  
	    UUID idReserva,
	    LocalDateTime dataInicial,
	    LocalDateTime dataFinal,
	    
	    // Dados do Estoque/Equipamento (Para o usuário ver o que comprou e o preço)
	    UUID idEstoque,
	    String nomeModelo,
	    Double preco
		
		
		) {
	
	

}
