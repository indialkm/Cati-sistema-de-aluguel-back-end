package com.cati.tcc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cati.tcc.model.HistoricoTransacao;
import com.cati.tcc.model.Pagamento;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.enums.StatusPagamento;
import com.cati.tcc.repository.HistoricoTransacaoRepository;

import jakarta.transaction.Transactional;

@Service
public class HistoricoTransacaoService {
	
	private final HistoricoTransacaoRepository histoRepository;
	private final PagamentoService pagService;
	private final UserService userService;
	
	
	
	public HistoricoTransacaoService(HistoricoTransacaoRepository histoRepository, PagamentoService pagService,
			UserService userService) {
		this.histoRepository = histoRepository;
		this.pagService = pagService;
		this.userService = userService;
	}


	@Transactional
    public void registrarMudanca(Pedido pedido, String descricao) {
		
		Pagamento pagamento = pagService.buscarPorpedido(pedido.getId());
		
		if(pagamento != null) {
        HistoricoTransacao historico = new HistoricoTransacao();
        
        historico.setPagamento(pagamento);
        historico.setStatus(pagamento.getStatus()); 
        historico.setDataEvento(LocalDateTime.now());
        historico.setDescricao(descricao);
        historico.setPedido(pedido);
        historico.setUser(pedido.getUser());
        
        historico.setPagamento(pagamento);
		historico.setStatus(pagamento.getStatus());
	
        histoRepository.save(historico);
        return;
        }
		
		HistoricoTransacao historico = new HistoricoTransacao();
        
        historico.setPagamento(null);
        historico.setPedido(pedido);
        historico.setStatus(pagamento.getStatus()); 
        historico.setDataEvento(LocalDateTime.now());
        historico.setDescricao(descricao);
        historico.setUser(pedido.getUser());
        histoRepository.save(historico);
		
    }
	
	
	public Page<HistoricoTransacao> consultarComoEmpresa(Pageable pageable) {
	    
	    return histoRepository.findAll(pageable); 
	}

	
	public Page<HistoricoTransacao> consultarComoCliente(UUID clienteId, Pageable pageable) {
	    
	    return histoRepository.buscarPorUsuarioId(clienteId, pageable);
	}

}
