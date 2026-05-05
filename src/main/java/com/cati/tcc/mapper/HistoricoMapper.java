package com.cati.tcc.mapper;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.response.HistoricoResponse;
import com.cati.tcc.dto.response.PagamentoResponse;
import com.cati.tcc.dto.response.UserResponse;
import com.cati.tcc.model.HistoricoTransacao;
import com.cati.tcc.model.Pagamento;
import com.cati.tcc.model.User;

@Component
public class HistoricoMapper {
	
	private final UserMapper userMapper;
	

    public HistoricoMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	public HistoricoResponse toResponse(HistoricoTransacao entity) {
        if (entity == null) return null;

        return new HistoricoResponse(
            entity.getId(),
            entity.getDataEvento(),
            entity.getDescricao(),
            entity.getStatus(),
            toPagamentoResponse(entity.getPagamento()),
            entity.getPedido() != null ? entity.getPedido().getId() : null,
            userMapper.toResponse(entity.getUser())
        );
    }

    private PagamentoResponse toPagamentoResponse(Pagamento pag) {
        if (pag == null) return null;
        
        return new PagamentoResponse(
            pag.getId(),
            pag.getPedido() != null ? pag.getPedido().getId() : null,
            pag.getValorPago(), // Certifique-se que o nome do campo é este na Entity
            pag.getDataPagamento(),
            pag.getStatus(),
            pag.getFormaPagamento(),
            pag.getTransacaoGatewayId(),
            pag.getClientSecret(),
            pag.getParcelas()
        );
    }

}