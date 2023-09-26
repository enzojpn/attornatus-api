package br.gov.sp.attornatus.attornatusapi.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PessoaNaoEncontradoException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public PessoaNaoEncontradoException(Long pessoaId) { 
		super(String.format("Não existe cadastro de pessoa com código %d", pessoaId));
	}
	 
	public PessoaNaoEncontradoException(String message) { 
		super(message);
	}

}
