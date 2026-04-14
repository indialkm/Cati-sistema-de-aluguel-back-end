package com.cati.tcc.model;

import java.time.LocalDateTime;

public class NotaPedido {
	
	// Dados do Emitente (Fornecedor)
    String emitenteRazaoSocial;
    String emitenteCnpj;

    // Dados do Tomador (Cliente)
    String clienteNome;
    String clienteCpfCnpj;
 
    // Dados do Pedido
    Double valorTotal;
    LocalDateTime dataEmissao;

}
