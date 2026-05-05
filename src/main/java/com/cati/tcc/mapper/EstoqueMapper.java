package com.cati.tcc.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.EstoqueRequest;
import com.cati.tcc.dto.response.DetalhesModeloResponse;
import com.cati.tcc.dto.response.EquipamentoResponse;
import com.cati.tcc.dto.response.EstoqueResponse;
import com.cati.tcc.dto.response.MidiaResponse;
import com.cati.tcc.model.Estoque;
import com.cati.tcc.model.Midia;
import com.cati.tcc.model.enums.TipoEstoque;

@Component
public class EstoqueMapper {

    private final EquipamentoMapper equipamentoMapper;
    private final MidiaMapper midiaMapper;

    public EstoqueMapper(EquipamentoMapper equipamentoMapper, MidiaMapper midiaMapper) {
        this.equipamentoMapper = equipamentoMapper;
        this.midiaMapper = midiaMapper;
    }

    public Estoque toEntity(EstoqueRequest request) {
        if (request == null) return null;

        Estoque estoque = new Estoque();
        estoque.setDescricao(request.descricao());
        estoque.setNome(request.nome());
        estoque.setPrecoBase(request.precoBase() == null ? 0.0 : request.precoBase());
        estoque.setCategoria(request.categoria());
        estoque.setAltura(request.largura());
        estoque.setAltura(request.altura());
        estoque.setTipoEstoque(TipoEstoque.valueOf(request.tipoEstoque()));
       
        return estoque;
    }

    public EstoqueResponse toResponse(Estoque estoque) {
        if (estoque == null) return null;

       
        List<EquipamentoResponse> equipamentosDTO = (estoque.getEquipamentos() != null) ?
            estoque.getEquipamentos().stream()
                .map(equipamentoMapper::toResponse)
                .toList() : new ArrayList<>();
        
        
        List<MidiaResponse> fotos = (estoque.getFotosModelos() != null) ?
        		 estoque.getFotosModelos().stream()
                .map(midiaMapper::toResponse) 
                .toList() : new ArrayList<>();

        return new EstoqueResponse(
            estoque.getId(),
            estoque.getQuantidade(),
            estoque.getDescricao(),
            estoque.getNome(),
            estoque.getPrecoBase(),
            fotos,
            estoque.getCategoria(),
            equipamentosDTO,
            estoque.getLargura(),
            estoque.getAltura(),
            estoque.getTipoEstoque().name()
            
        );
    }

    public DetalhesModeloResponse toDetalhesResponse(Estoque estoque, Double precoCalculado, boolean disponivel) {
        if (estoque == null) return null;

        List<String> urlsFotos = (estoque.getFotosModelos() != null) ?
        	    estoque.getFotosModelos().stream()
        	        .map(midiaMapper::toResponse) 
        	        .map(MidiaResponse::url)     
        	        .toList() : new ArrayList<>();

        return new DetalhesModeloResponse(
            estoque.getId(),
            estoque.getNome(),
            estoque.getDescricao(),
            urlsFotos.isEmpty() ? null : urlsFotos.get(0),
            urlsFotos,
            precoCalculado, 
            estoque.getAltura(),
            estoque.getLargura(),
            estoque.getPrecoBase(),
            estoque.getCategoria(),
            disponivel
        );
    }
}