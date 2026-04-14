package com.cati.tcc.config.exceptions;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErroResposta {
	
	private LocalDateTime timestamp;
    private int status;
    private String erro;
    private String mensagem;
    private String path;

}
