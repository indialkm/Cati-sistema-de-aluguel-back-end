package com.cati.tcc.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.PagamentoRequest;
import com.cati.tcc.dto.response.PagamentoResponse;
import com.cati.tcc.model.Pagamento;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.enums.StatusPagamento;


@Component
public class PagamentoMapper {
	
	public PagamentoResponse toResponse(Pagamento pagamento) {
        if (pagamento == null) return null;

        return new PagamentoResponse(
                pagamento.getId(),
                pagamento.getPedido().getId(),
                pagamento.getValorPago(),
                pagamento.getDataPagamento(),
                pagamento.getStatus(),
                pagamento.getFormaPagamento(),
                pagamento.getTransacaoGatewayId(),
                pagamento.getClientSecret(), 
                pagamento.getParcelas()
            );
    }

	public Pagamento toEntity(PagamentoRequest request, Pedido pedido) {
        if (request == null) return null;

        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido); 
        pagamento.setDataPagamento(LocalDateTime.now()); 
        pagamento.setStatus(StatusPagamento.PENDENTE);  
        pagamento.setFormaPagamento(request.formaPagamento());
        //pagamento.setTransacaoGatewayId(request.);
        pagamento.setParcelas(request.parcelas());

        return pagamento;
    }
	

}
