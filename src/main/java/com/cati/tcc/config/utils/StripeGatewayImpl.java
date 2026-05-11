package com.cati.tcc.config.utils;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.cati.tcc.config.payment.GatewayReponse;
import com.cati.tcc.dto.request.PagamentoRequest;
import com.cati.tcc.repository.PagamentoGateway;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;

@Component
@Primary
public class StripeGatewayImpl implements PagamentoGateway {
	
	// O CONSTRUTOR É O LUGAR IDEAL
    public StripeGatewayImpl() {
        
        Stripe.apiKey = "sk_test_51TMxxH9E0qFEn7nqq8MtYu7mv8Kp4FFiw5ul1u4zlPHuUJHXud685"
        		+ "j5vmdZsxfDNFeSfBIpuvAYXozdCsDSkH72I00TnYhtqOb";
    }
	
	@Override
    public GatewayReponse criarIntencao(PagamentoRequest request) {
        try {
        	
            // O Stripe trabalha com centavos (Long), por isso multiplicamos por 100
            long valorEmCentavos = (long) (request.valorPago() * 100);

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(valorEmCentavos)
                .setCurrency("brl")
                .putMetadata("pedido_id", request.idPedido().toString())
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .build()
                )
                .build();

            PaymentIntent intent = PaymentIntent.create(params);
            
            
            return new GatewayReponse(intent.getId(), intent.getClientSecret());
            
        } catch (StripeException e) {
            throw new RuntimeException("Erro na comunicação com o Stripe: " + e.getMessage());
        }
    }

	@Override
	public void estornar(String transacaoId, Double valor) {
	    try {
	       
	        long valorEmCentavos = (long) (valor * 100);

	        RefundCreateParams params = RefundCreateParams.builder()
	            .setPaymentIntent(transacaoId) 
	            .setAmount(valorEmCentavos)    
	            .build();

	        Refund refund = Refund.create(params);
	        
	        System.out.println("Estorno realizado: " + refund.getId());
	    } catch (StripeException e) {
	        throw new RuntimeException("Erro ao estornar: " + e.getMessage());
	    }
	}
}