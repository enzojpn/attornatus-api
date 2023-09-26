package br.gov.sp.attornatus.attornatusapi.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EnderecoNaoEncontradoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
 
	public EnderecoNaoEncontradoException(Long enderecoId) { 
		super(String.format("Não existe cadastro de endereço com código %d", enderecoId));
	}

}
