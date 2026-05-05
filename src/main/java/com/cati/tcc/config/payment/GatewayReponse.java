package com.cati.tcc.config.payment;

public class GatewayReponse {
	String idTransacao;
    String clientSecret;
	
    
    public GatewayReponse(String idTransacao, String clientSecret) {
		this.idTransacao = idTransacao;
		this.clientSecret = clientSecret;
	}


	public String getIdTransacao() {
		return idTransacao;
	}


	public void setIdTransacao(String idTransacao) {
		this.idTransacao = idTransacao;
	}


	public String getClientSecret() {
		return clientSecret;
	}


	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
    
    

}
