package com.cati.tcc.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cati.tcc.config.exceptions.NegocioException;
import com.cati.tcc.dto.request.ChecklistRequest;
import com.cati.tcc.dto.response.AluguelResponse;
import com.cati.tcc.mapper.AluguelMapper;
import com.cati.tcc.model.Aluguel;
import com.cati.tcc.model.Checklist;
import com.cati.tcc.model.ItemPedido;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.User;
import com.cati.tcc.model.enums.FormasPagamento;
import com.cati.tcc.model.enums.StatusAluguel;
import com.cati.tcc.model.enums.StatusItemPedido;
import com.cati.tcc.model.enums.StatusReserva;
import com.cati.tcc.repository.AluguelRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class AluguelService {
	
	private final AluguelMapper aluguelMapper;
	private final AluguelRepository aluguelRepository;
	
	private final PedidoService pedidoService;
	private final ChecklistService checklistService;
	private final ItemPedidoService itemService;
	private final PagamentoService pag;
	private final AuthService auth;
	private final UserService userService;
	private final EstoqueService estoqueService;
	
	public AluguelService(AluguelMapper aluguelMapper, AluguelRepository aluguelRepository, PedidoService pedidoService,
			ChecklistService checklistService, ItemPedidoService itemService, PagamentoService pag, AuthService auth,
			UserService userService, EstoqueService estoque) {
		this.aluguelMapper = aluguelMapper;
		this.aluguelRepository = aluguelRepository;
		this.pedidoService = pedidoService;
		this.checklistService = checklistService;
		this.itemService = itemService;
		this.pag = pag;
		this.auth = auth;
		this.userService = userService;
		this.estoqueService = estoque;
	}


	/**
	 * Lógica centralbuscar aluguel por id.
	 * @param o UUID do Aluguel que será encontrado no banco de dados.
	 * @return o objeto Aluguel populado com as informações do banco
	 * @throws EntityNotFoundExeption, o aluguel não foi encontrado no banco.
	 */
	@Transactional
	public Aluguel buscarAluguel(UUID idAluguel) {
		
		return aluguelRepository.findById(idAluguel)
	            .orElseThrow(() -> new EntityNotFoundException("Aluguel não encontrado"));
		
	}

	
	/**
	 * Lógica central para gerar aluguéis depois de pedido pago.
	 * @param o UUID do pedido, pois usaremos as informações do pedido e ds itens pedido para popular cada aluguel.
	 * @return A List<Aluguel> com cada item pedido trasmutado para Aluguel e um novo ciclo de vida
	 * @throws Tenho que colocar um exception.
	 */
	public List<Aluguel> gerarAlugueisParaPedido(Pedido pedido) {
    
      
		UUID idUsuarioLogado = auth.getAuthenticatedUserId();
		User user = userService.buscarId(idUsuarioLogado);
		
		Queue<ItemPedido> filaItens = new LinkedList<>(pedido.getItensPedidos());
        List<Aluguel> alugueisProcessados = new ArrayList<>();
		String formaPagamento = pag.pegarFormaDepagamento(pedido.getId());
        
		while(!filaItens.isEmpty()) {
			
			ItemPedido item = filaItens.poll();
			
			Aluguel novoAluguel = new Aluguel();
			
			novoAluguel.setIdItemPedido(item.getId());
			novoAluguel.setIdPedido(pedido.getId());
			novoAluguel.setEstoque(item.getEstoque());
			novoAluguel.setReserva(item.getReserva());
			novoAluguel.setEndereco(item.getEnderecoEntrega());
			novoAluguel.setFormaDePagamento(formaPagamento);
			novoAluguel.setPreco(item.getPreco());
			novoAluguel.setUser(user);
			
			alugueisProcessados.add(aluguelRepository.save(novoAluguel));
			
		}
		
		return alugueisProcessados.stream()
        		.peek(i -> aluguelRepository.save(i))
        		.toList();
		
    }
	
	public Aluguel buscarPorId(UUID id) {
		return aluguelRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Aluguel não encontrado"));
	}
	
	
	public List<Aluguel> gerarAlugueisParaPedidoTeste(Pedido pedido) {
	    
	      
		UUID idUsuarioLogado = auth.getAuthenticatedUserId();
		User user = userService.buscarId(idUsuarioLogado);
		
		Queue<ItemPedido> filaItens = new LinkedList<>(pedido.getItensPedidos());
        List<Aluguel> alugueisProcessados = new ArrayList<>();
		
        
		while(!filaItens.isEmpty()) {
			
			ItemPedido item = filaItens.poll();
			
			Aluguel novoAluguel = new Aluguel();
			
			novoAluguel.setIdItemPedido(item.getId());
			novoAluguel.setIdPedido(pedido.getId());
			novoAluguel.setEstoque(item.getEstoque());
			novoAluguel.setReserva(item.getReserva());
			novoAluguel.setEndereco(item.getEnderecoEntrega());
			novoAluguel.setFormaDePagamento("PIX");
			novoAluguel.setPreco(item.getPreco());
			novoAluguel.setUser(user);
			
			alugueisProcessados.add(aluguelRepository.save(novoAluguel));
			
		}
		
		return alugueisProcessados.stream()
        		.peek(i -> aluguelRepository.save(i))
        		.toList();
		
    }
	
	
	public Page<Aluguel> listarTodos(Pageable paginacao) {
       
        Page<Aluguel> alugueis = aluguelRepository.findAll(paginacao);
        return alugueis;
    }


	public Page<List<Aluguel>> alugueisPorUsuario(Pageable pageable){
		
		UUID idUsuarioLogado = auth.getAuthenticatedUserId();
		User user = userService.buscarId(idUsuarioLogado);
		
		return aluguelRepository.findByUser(user, pageable);
		
	}

	public List<Aluguel> alugueisPorPedido(UUID idPedido){
		
		return aluguelRepository.findByIdPedido(idPedido)
				 .orElseThrow(() -> new EntityNotFoundException("Aluguel não encontrado"));
	}
	
	@Transactional()
    public Page<Aluguel> buscarComFiltroStatus(List<StatusAluguel> status, Pageable pageable) {
        if (status == null || status.isEmpty()) {
            return aluguelRepository.findAll(pageable);
        }
        return aluguelRepository.findByStatusIn(status, pageable);
    }
	
	@Transactional 
	public Aluguel atualizarAlugueis(Aluguel aluguel){
		return aluguelRepository.save(aluguel);
	}
	
	/***************MUDAR STATUS********************/
	@Transactional 
	public Aluguel statusAguardando(UUID idAluguel)
	{
		Aluguel aluguel = buscarAluguel(idAluguel);
		
		aluguel.setStatus(StatusAluguel.AGUARDANDO);
		
		return aluguelRepository.save(aluguel);
	}

    private Aluguel executarFluxoFinalizacao(Aluguel aluguel) {
        boolean temEntrada = aluguel.getChecklistEntrada() != null && !aluguel.getChecklistEntrada().getMidias().isEmpty();
        boolean temSaida = aluguel.getChecklistSaida() != null && !aluguel.getChecklistSaida().getMidias().isEmpty();
        
        if (!temEntrada || !temSaida) {
            throw new NegocioException("É necessário ter as mídias de checklists de ENTRADA e SAÍDA antes de finalizar.");
        }

        finalizarStatusReserva(aluguel.getId());
        estoqueService.retornarEstoque(aluguel.getEstoque().getId(), 1);
        
        aluguel.setStatus(StatusAluguel.FINALIZADO);
        return aluguelRepository.save(aluguel);
    }


    public Aluguel statusCancelado(UUID idAluguel) {
        Aluguel aluguel = buscarAluguel(idAluguel);
        aluguel.setStatus(StatusAluguel.CANCELADO);
        return aluguelRepository.save(aluguel);
        
        
    }
    
    @Transactional
    public Aluguel mudarStatus(UUID id,String statusString) {
        
    	Aluguel aluguel = buscarAluguel(id); 
    	String statusLimpo = statusString.replace("\"", "").trim().toUpperCase();
        StatusAluguel novoStatus = StatusAluguel.valueOf(statusLimpo.toUpperCase());
       
        
        switch (novoStatus) {
            case FINALIZADO:
                return executarFluxoFinalizacao(aluguel);
            
            case CANCELADO:
                
                aluguel.setStatus(StatusAluguel.CANCELADO);
                break;

            default:
                
                aluguel.setStatus(novoStatus);
                break;
        }

        return aluguelRepository.save(aluguel);
    }
    
    /*************************************************************/
    
    
    /**** Lógica de montagem e desmontagem ***/
    

	@Transactional
    public Aluguel statusMontada(UUID idAluguel, ChecklistRequest checklist, List<MultipartFile> arquivos) {
        Aluguel aluguel = buscarAluguel(idAluguel);
        aluguel.setChecklistEntrada(registrarVistoria(idAluguel, checklist, arquivos));
        aluguel.setStatus(StatusAluguel.MONTADA);
        return aluguelRepository.save(aluguel);

    }

    @Transactional
    public Aluguel statusRetirada(UUID idAluguel, ChecklistRequest checklist, List<MultipartFile> arquivos) {
        Aluguel aluguel = buscarAluguel(idAluguel);
        aluguel.setChecklistSaida(registrarVistoriaSaida(idAluguel, checklist, arquivos));
        aluguel.setStatus(StatusAluguel.RETIRADA);
        return aluguelRepository.save(aluguel);
    }
    
    
    public Checklist buscarChecklistEntradaporAluguel(UUID idAluguel){
    	return aluguelRepository.buscarChecklistEntradaPorAluguelId(idAluguel)
    				.orElseThrow(() -> new EntityNotFoundException("Checklist de entrada não inserido"));
    	
    }
    
    
    public Checklist buscarChecklistSaidaporAluguel(UUID idAluguel){ 	
    	return aluguelRepository.buscarChecklistSaidaPorAluguelId(idAluguel)
    				.orElseThrow(() -> new EntityNotFoundException("Checklist de saida não inserido"));
    	
    }
    
    @Transactional
     public Checklist registrarVistoria(UUID aluguelId, ChecklistRequest request,List<MultipartFile> arquivos) {
           Aluguel aluguel = buscarAluguel(aluguelId);
           return checklistService.entrada(aluguel, request, arquivos);
        }
     
    @Transactional
     public Checklist registrarVistoriaSaida(UUID aluguelId, ChecklistRequest request, List<MultipartFile> arquivos) {
     
       Aluguel aluguel = buscarAluguel(aluguelId);
       return checklistService.saida(aluguel, request, arquivos);
     }
    
    
    /********************************************************/

    //Se pelo menos um item do pedido for diferente de finalizado ele retorna false
	@Transactional
	public boolean fecharAluguel(UUID pedidoId) {
		
		List<Aluguel> alugueis = alugueisPorPedido(pedidoId);
		
		return alugueis.stream()
		.allMatch( i -> i.getStatus() == StatusAluguel.FINALIZADO);
		

	}
	
	@Transactional
	public Aluguel finalizarStatusReserva(UUID idAluguel){
		Aluguel aluguel = buscarPorId(idAluguel);
		aluguel.getReserva().setDisponibilidade(StatusReserva.CONCLUIDA);
		return aluguel;
	}
	
}
    
   