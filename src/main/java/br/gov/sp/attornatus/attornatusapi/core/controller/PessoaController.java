package br.gov.sp.attornatus.attornatusapi.core.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.gov.sp.attornatus.attornatusapi.core.model.Endereco;
import br.gov.sp.attornatus.attornatusapi.core.model.Pessoa;
import br.gov.sp.attornatus.attornatusapi.core.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

	@Autowired
	private PessoaService pessoaService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Pessoa salvar(@RequestBody Pessoa pessoa) {
		return pessoaService.criarPessoa(pessoa);

	}

	@PutMapping("/{pessoaId}")
	public Pessoa alterar(@PathVariable Long pessoaId, @RequestBody Pessoa pessoa) {

		Pessoa pessoaAtual = pessoaService.buscarOuFalhar(pessoaId);

		BeanUtils.copyProperties(pessoa, pessoaAtual, "id");

		return pessoaService.criarPessoa(pessoaAtual);
	}

	@GetMapping("/{pessoaId}")
	public Pessoa buscar(@PathVariable Long pessoaId) {
		return pessoaService.buscarOuFalhar(pessoaId);
	}

	@GetMapping()
	public List<Pessoa> listar(){
		return pessoaService.listar();
	}

	@GetMapping("/{pessoaId}/enderecos")
	public List<Endereco> listarEnderecosDaPessoa(@PathVariable Long pessoaId){
		return pessoaService.buscarEnderecosPorIdPessoa(pessoaId);
	}

	@PutMapping("/{pessoaId}/endereco/{enderecoId}/principal")  
	public void enderecoPrincipal(@PathVariable Long pessoaId, @PathVariable Long enderecoId) {
		pessoaService.setEnderecoPrincipal(pessoaId ,enderecoId);
	}

	@GetMapping("/{pessoaId}/endereco-principal")
	public Endereco buscarEnderecoPrincipal(@PathVariable Long pessoaId){
		return pessoaService.buscarEnderecosEnderecoPrincipal(pessoaId);
	}
}
