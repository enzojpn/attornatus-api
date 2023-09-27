package br.gov.sp.attornatus.attornatusapi.core.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.gov.sp.attornatus.attornatusapi.core.model.Endereco;
import br.gov.sp.attornatus.attornatusapi.core.service.EnderecoService;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

	@Autowired
	private EnderecoService enderecoService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Endereco salvar(@RequestBody Endereco endereco) {
		return enderecoService.criarEndereco(endereco);

	}

	@GetMapping
	public List<Endereco> listar(){
		
		return enderecoService.listarEnderecos();
	}

	@GetMapping("/por-pessoa-id/{pessoaId}")
	public List<Endereco> listarPorIdPessoa(@PathVariable Long pessoaId  ){
		
		return enderecoService.listarEnderecosPorIdPessoa(pessoaId);
	}
 
	
}
