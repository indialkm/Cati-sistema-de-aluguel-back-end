package com.cati.tcc.model.enums;

public enum StatusAluguel {
	
	AGUARDANDO, // Pago, mas ainda não saiu
    MONTAGEM,     // Saiu para montagem 
    MONTADA, 	// Checklist de entrada feito, cliente usando
    RETIRADA,   // Cliente devolvendo
    CONFERENCIA, // O user precisa dar ok 
    FINALIZADO,          // Tudo certo!
    AVARIADO             // Voltou com problema 
    , CANCELADO

}
