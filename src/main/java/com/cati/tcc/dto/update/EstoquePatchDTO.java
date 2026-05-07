package com.cati.tcc.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Optional;

import com.cati.tcc.model.enums.TipoEstoque;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoquePatchDTO {
    
    private Optional<String> nome = Optional.empty();
    private Optional<String> descricao = Optional.empty();
    private Optional<Double> precoBase = Optional.empty();
    private Optional<String> categoria = Optional.empty();
    private Optional<Double> altura = Optional.empty();
    private Optional<Double> largura = Optional.empty();
    private Optional<TipoEstoque> tipoEstoque = Optional.empty();
}