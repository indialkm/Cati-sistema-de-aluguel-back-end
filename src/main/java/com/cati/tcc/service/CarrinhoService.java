package com.cati.tcc.service;

import java.util.UUID;
import org.springframework.stereotype.Service;

import com.cati.tcc.dto.request.ItemCarrinhoRequest;
import com.cati.tcc.model.Carrinho;
import com.cati.tcc.model.User;
import com.cati.tcc.model.enums.StatusCarrinho;
import com.cati.tcc.repository.CarrinhoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final AuthService auth;
    private final UserService userService;
    private final ItemCarrinhoService itemService;
    private final RegraPrecoService precoService;

    public CarrinhoService(CarrinhoRepository carrinhoRepository, AuthService auth,
                          UserService userService, ItemCarrinhoService itemService, 
                          RegraPrecoService precoService) {
        this.carrinhoRepository = carrinhoRepository;
        this.auth = auth;
        this.userService = userService;
        this.itemService = itemService;
        this.precoService = precoService;
    }

    /**
     * Ponto de entrada principal para adicionar itens. 
     * Resolve se o carrinho é novo, existente ou se precisa de merge.
     */
    @Transactional
    public Carrinho adicionarItem(UUID idCarrinhoExistente, ItemCarrinhoRequest request) {
        UUID idUsuarioLogado = auth.getAuthenticatedUserId();
        Carrinho carrinho;

        if (idUsuarioLogado != null) {
            carrinho = obterCarrinhoUsuarioLogado(idUsuarioLogado, idCarrinhoExistente);
        } else {
            
            carrinho = obterCarrinhoAnonimo(idCarrinhoExistente);
        }

        processarAdicaoItem(carrinho, request);
        
        return atualizarTotalESalvar(carrinho);
    }

    private Carrinho obterCarrinhoUsuarioLogado(UUID userId, UUID idCarrinhoAnonimo) {
        Carrinho oficial = carrinhoRepository.findByUserIdAndStatusCarrinho(userId, StatusCarrinho.ABERTO)
                .orElseGet(() -> criarNovoCarrinhoParaUsuario(userId));

       
        if (idCarrinhoAnonimo != null) {
            mesclarItens(idCarrinhoAnonimo, oficial);
        }

        return oficial;
    }

    private Carrinho obterCarrinhoAnonimo(UUID idCarrinhoExistente) {
        if (idCarrinhoExistente != null) {
            return carrinhoRepository.findById(idCarrinhoExistente)
                    .orElseGet(this::criarNovoCarrinhoAnonimo);
        }
        return criarNovoCarrinhoAnonimo();
    }

    private void processarAdicaoItem(Carrinho carrinho, ItemCarrinhoRequest request) {
        boolean jaExiste = carrinho.getItens().stream()
                .anyMatch(item -> item.getReserva().getId().equals(request.idReserva()));

        if (!jaExiste) {
            itemService.criar(carrinho, request);
        }
    }

    private void mesclarItens(UUID idCarrinhoFonte, Carrinho carrinhoDestino) {
        carrinhoRepository.findById(idCarrinhoFonte).ifPresent(fonte -> {
            
            if (fonte.getUser() == null) {
                fonte.getItens().forEach(item -> {
                   
                    boolean duplicado = carrinhoDestino.getItens().stream()
                        .anyMatch(i -> i.getReserva().getId().equals(item.getReserva().getId()));
                    
                    if (!duplicado) {
                        item.setCarrinho(carrinhoDestino);
                        carrinhoDestino.getItens().add(item);
                    }
                });
                
                fonte.getItens().clear(); 
                carrinhoRepository.delete(fonte);
            }
        });
    }

    private Carrinho criarNovoCarrinhoParaUsuario(UUID userId) {
        User user = userService.buscarId(userId);
        Carrinho novo = new Carrinho();
        novo.setUser(user);
        novo.setStatusCarrinho(StatusCarrinho.ABERTO);
        novo.setTotal(0.0);
        return carrinhoRepository.save(novo);
    }

    private Carrinho criarNovoCarrinhoAnonimo() {
        Carrinho novo = new Carrinho();
        novo.setStatusCarrinho(StatusCarrinho.ABERTO);
        novo.setTotal(0.0);
        return carrinhoRepository.save(novo);
    }

    private Carrinho atualizarTotalESalvar(Carrinho carrinho) {
        Double total = precoService.calcularTotalCarrinho(carrinho.getItens());
        carrinho.setTotal(total);
        return carrinhoRepository.save(carrinho);
    }
    
    @Transactional
    public Carrinho buscarCarrinhoAtivo() {
    	UUID userId = auth.getAuthenticatedUserId();
        return carrinhoRepository.findByUserIdAndStatusCarrinho(userId, StatusCarrinho.ABERTO)
                .orElseGet(() -> {
                    return null; 
                });
    }

    // Métodos de consulta simples mantidos para utilidade
    public Carrinho buscarCarrinhoId(UUID id) {
    	
    	
        return carrinhoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Carrinho não encontrado"));
    }

    @Transactional
	public void inativarCarrinho(UUID id) {
		// TODO Auto-generated method stub
		Carrinho cart = buscarCarrinhoId(id);
		cart.setStatusCarrinho(StatusCarrinho.FECHADO);
		carrinhoRepository.save(cart);
	}
}