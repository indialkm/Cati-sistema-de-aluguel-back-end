package com.cati.tcc.service;

import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cati.tcc.config.security.UserSpringSecurity;
import com.cati.tcc.dto.request.ItemCarrinhoRequest;
import com.cati.tcc.mapper.CarrinhoMapper;
import com.cati.tcc.model.Carrinho;
import com.cati.tcc.model.ItemCarrinho;
import com.cati.tcc.model.User;
import com.cati.tcc.model.enums.StatusCarrinho;
import com.cati.tcc.repository.CarrinhoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final CarrinhoMapper carrinhoMapper;
    private final AuthService auth;
    
    private final UserService userService;
    private final ItemCarrinhoService itemService;
	

    public CarrinhoService(CarrinhoRepository carrinhoRepository, CarrinhoMapper carrinhoMapper, AuthService auth,
			UserService userService, ItemCarrinhoService itemService) {
		this.carrinhoRepository = carrinhoRepository;
		this.carrinhoMapper = carrinhoMapper;
		this.auth = auth;
		this.userService = userService;
		this.itemService = itemService;
	}

    @Transactional
    public Carrinho buscarCarrinhoAtivo(UUID id) {
    	
    	return carrinhoRepository.findByUserIdAndStatusCarrinho(id, StatusCarrinho.ABERTO)
    			.orElseThrow(() -> new EntityNotFoundException("Carrinho não enocntrado apra esse usuário"));
    	
    }

	@Transactional
    public Carrinho buscarOuCriarCarrinho(Optional<UUID> idCarrinhoFantasma) {
    
        UUID idUsuarioLogado = auth.getAuthenticatedUserId();

        // 2. Busca ou Cria (Sua lógica está perfeita aqui)
        Carrinho carrinhoOficial = carrinhoRepository.findByUserIdAndStatusCarrinho(idUsuarioLogado, StatusCarrinho.ABERTO)
                .orElseGet(() -> {
                    User user = userService.buscarId(idUsuarioLogado);
                    Carrinho novo = new Carrinho();
                    novo.setUser(user);
                    novo.setStatusCarrinho(StatusCarrinho.ABERTO);
                    novo.setTotal(0.0);
                    return carrinhoRepository.save(novo);
                });

        // 3. Mesclagem (Sua lógica está perfeita aqui)
        idCarrinhoFantasma.ifPresent(id -> mesclarItens(id, carrinhoOficial));

        return carrinhoOficial;
    }
	
	
    
    
    private void mesclarItens(UUID idCarrinhoFantasma, Carrinho carrinhoDestino) {
        carrinhoRepository.findById(idCarrinhoFantasma).ifPresent(fantasma -> {
            
            // Se o fantasma for de outro usuário (segurança), a gente nem mexe
            if (fantasma.getUser() == null && !fantasma.getItens().isEmpty()) {
                
                for (ItemCarrinho item : fantasma.getItens()) {
                    item.setCarrinho(carrinhoDestino); // Transfere o item para o novo "pai"
                    carrinhoDestino.getItens().add(item);
                }
                
                fantasma.getItens().clear(); // Limpa o antigo para não dar erro de persistência
                carrinhoRepository.delete(fantasma); // Mata o carrinho fantasma
                
                carrinhoDestino.atualizarTotal(); // Recalcula o valor com os novos itens
            }
        });
    }
    
    @Transactional
    public Carrinho adicionarItem(HttpSession session, ItemCarrinhoRequest request) {
        Carrinho carrinho;

        // 1. Tenta pegar o usuário do SecurityContext
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserSpringSecurity userDetails) {
            // --- FLUXO LOGADO ---
            // Recupera o ID fantasma que estava na sessão antes do login (se existir)
            UUID idFantasma = (UUID) session.getAttribute("CARRINHO_ID");
            
            // Busca o carrinho oficial e já tenta mesclar o fantasma
            carrinho = buscarOuCriarCarrinho(Optional.ofNullable(idFantasma));
            
            // Limpa a sessão pois agora o carrinho é o oficial do banco
            session.removeAttribute("CARRINHO_ID");
        } else {
            // --- FLUXO ANÔNIMO ---
            UUID carrinhoId = (UUID) session.getAttribute("CARRINHO_ID");
            if (carrinhoId != null) {
                carrinho = carrinhoRepository.findById(carrinhoId)
                        .orElseGet(() -> criarNovoCarrinhoAnonimo(session));
            } else {
                carrinho = criarNovoCarrinhoAnonimo(session);
            }
        }

        boolean reservaJaEstaNoCarrinho = carrinho.getItens().stream()
                .anyMatch(item -> item.getReserva().getId().equals(request.idReserva()));

        if (!reservaJaEstaNoCarrinho) {
            // Só cria se essa reserva específica ainda não estiver lá
            itemService.criar(carrinho, request);
        }
        
        // 3. Atualiza o total e salva
        carrinho.atualizarTotal(); 
        return carrinhoRepository.save(carrinho);
    }
    
    private Carrinho criarNovoCarrinhoAnonimo(HttpSession session) {
        Carrinho novo = new Carrinho();
        novo.setStatusCarrinho(StatusCarrinho.ABERTO);
        novo = carrinhoRepository.save(novo);
        
        // GUARDA NA SESSÃO: O Spring Session vai salvar isso no banco de dados automaticamente
        session.setAttribute("CARRINHO_ID", novo.getId());
        
        return novo;
    }
    


    @Transactional
    public void inativarCarrinho(UUID idCarrinho) {
    	
    	Carrinho carrinho = carrinhoRepository.findById(idCarrinho)
    			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrinho não encontrado"));
    	
    	carrinho.setStatusCarrinho(StatusCarrinho.FECHADO);
    	carrinhoRepository.save(carrinho);
    	
    }
   
}