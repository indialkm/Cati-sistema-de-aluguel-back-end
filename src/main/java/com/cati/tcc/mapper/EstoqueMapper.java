package com.cati.tcc.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.EstoqueRequest;
import com.cati.tcc.dto.response.EquipamentoResponse;
import com.cati.tcc.dto.response.EstoqueResponse;
import com.cati.tcc.model.Estoque;

@Component
public class EstoqueMapper {
	/*Nesse caso estou populando estoque com o equipamento mapper porque não faz sentindo mostrar um eestoque vazio**/
	private final EquipamentoMapper equipamentoMapper;

    public EstoqueMapper(EquipamentoMapper equipamentoMapper) {
        this.equipamentoMapper = equipamentoMapper;
    }

    public Estoque toEntity(EstoqueRequest request) {
        if (request == null) return null;

        Estoque estoque = new Estoque();
        estoque.setQuantidade(request.quantidade().orElse(0)); 
        estoque.setDescricao(request.descricao().orElse("Sem descrição"));
        estoque.setNome(request.nome());
        estoque.setPrecoBase(request.precoBase().orElse(0.0));
        estoque.setFotosModelos(request.fotosModelos());
        estoque.setCategoria(request.categoria());

        return estoque;
    }

    public EstoqueResponse toResponse(Estoque estoque) {
        if (estoque == null) return null;

        List<EquipamentoResponse> equipamentosDTO = null;

        if (estoque.getEquipamentos() != null) {
            equipamentosDTO = estoque.getEquipamentos()
                    .stream()
                    .map(equipamentoMapper::toResponse)
                    .toList();
        }

        return new EstoqueResponse(
            estoque.getId(),
            estoque.getQuantidade(),
            estoque.getDescricao(),
            estoque.getNome(),
            estoque.getPrecoBase(),
            estoque.getFotosModelos(),
            estoque.getCategoria(),
            equipamentosDTO
        );
    }

}
