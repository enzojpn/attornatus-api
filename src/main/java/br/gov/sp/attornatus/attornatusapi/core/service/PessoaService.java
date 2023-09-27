package br.gov.sp.attornatus.attornatusapi.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.sp.attornatus.attornatusapi.core.exception.PessoaNaoEncontradoException;
import br.gov.sp.attornatus.attornatusapi.core.model.Endereco;
import br.gov.sp.attornatus.attornatusapi.core.model.Pessoa;
import br.gov.sp.attornatus.attornatusapi.core.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository pessoaRepository;

	public Pessoa criarPessoa(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}

	public Pessoa buscarOuFalhar(Long pessoaId) {

		return pessoaRepository.findById(pessoaId).orElseThrow(() -> new PessoaNaoEncontradoException(pessoaId));

	}

	public List<Pessoa> listar() {
		return pessoaRepository.findAll();
	}

	public List<Endereco> buscarEnderecosPorIdPessoa(Long pessoaId) {
		return pessoaRepository.findEnderecosByPessoaId(pessoaId);
	}
 
    @Transactional
	public void setEnderecoPrincipal(Long pessoaId, Long restauranteId) {
    	Pessoa pessoa = buscarOuFalhar(pessoaId);
    	pessoa.setEnderecoPrincipalId(restauranteId);
	}

	public Endereco buscarEnderecosEnderecoPrincipal(Long pessoaId) {
		return pessoaRepository.findEnderecoPrincipalByPessoaId(pessoaId);
	}

}
