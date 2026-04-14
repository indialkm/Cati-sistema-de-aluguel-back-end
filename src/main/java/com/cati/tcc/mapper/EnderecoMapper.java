package com.cati.tcc.mapper;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.EnderecoRequest;
import com.cati.tcc.dto.response.EnderecoResponse;
import com.cati.tcc.model.Endereco;

@Component
public class EnderecoMapper {
	
	public Endereco toEntity(EnderecoRequest request)
	{
		Endereco end = new Endereco();
		end.setCep(request.cep());
		end.setLogradouro(request.logradouro());
		end.setNumero(request.numero());
		end.setComplemento(request.complemento());
		end.setBairro(request.bairro());
		end.setCidade(request.cidade());
		end.setUf(request.uf());
		
		return end;
	}
	
	public EnderecoResponse toResponse(Endereco end)
	{
		return new EnderecoResponse(
			end.getId(),
			end.getCep(),
			end.getLogradouro(),
			end.getNumero(),
			end.getComplemento(),
			end.getBairro(),
			end.getCidade(),
			end.getUf());
		
	}

}
