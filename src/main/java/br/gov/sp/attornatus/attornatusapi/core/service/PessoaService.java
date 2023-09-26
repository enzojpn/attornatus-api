package br.gov.sp.attornatus.attornatusapi.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.sp.attornatus.attornatusapi.core.model.Pessoa;
import br.gov.sp.attornatus.attornatusapi.core.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa criarPessoa(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}
	
	
}
