package br.gov.sp.attornatus.attornatusapi.core.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.sp.attornatus.attornatusapi.core.exception.PessoaNaoEncontradoException;
import br.gov.sp.attornatus.attornatusapi.core.model.Endereco;
import br.gov.sp.attornatus.attornatusapi.core.model.Pessoa;
import br.gov.sp.attornatus.attornatusapi.core.repository.EnderecoRepository;
import br.gov.sp.attornatus.attornatusapi.core.repository.PessoaRepository;

@Service
public class EnderecoService {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	public Endereco criarEndereco(Endereco endereco) {

		Long pessoaId = 0L;
		if (endereco.getPessoa() != null) {
			pessoaId = endereco.getPessoa().getId();
		} else {
			throw new PessoaNaoEncontradoException("Objeto Pessoa esta vazio");

		}
		Optional<Pessoa> optionalPessoa = pessoaRepository.findById(pessoaId);
		if (optionalPessoa.isPresent()) {
			Pessoa pessoa = optionalPessoa.get();
			endereco.setPessoa(pessoa);

		} else {

			throw new PessoaNaoEncontradoException(pessoaId);
		}
		return enderecoRepository.save(endereco);
	}

	public List<Endereco> listarEnderecos() {

		return enderecoRepository.findAll();

	} 

	public List<Endereco> listarEnderecosPorIdPessoa(Long pessoaId) {

		return enderecoRepository.findEnderecoByPessoaId(pessoaId);

	}

}
