package br.gov.sp.attornatus.attornatusapi.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.gov.sp.attornatus.attornatusapi.core.exception.PessoaNaoEncontradoException;
import br.gov.sp.attornatus.attornatusapi.core.model.Endereco;
import br.gov.sp.attornatus.attornatusapi.core.model.Pessoa;
import br.gov.sp.attornatus.attornatusapi.core.repository.PessoaRepository;
import br.gov.sp.attornatus.attornatusapi.core.service.PessoaService;

@SpringBootTest
@AutoConfigureMockMvc
public class PessoaServiceTest {

	@MockBean
	PessoaRepository pessoaRepository;

	@Autowired
	PessoaService pessoaService;

	@Test
	void deveRetornarPessoa_QuandoCriarPessoa() throws Exception {

		var pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Maria Santos");
		Date dataNascimento = Date.from(Instant.parse("2003-11-19T00:00:00.000+00:00"));
		pessoa.setDataNascimento(dataNascimento);

		Mockito.when(pessoaRepository.save(Mockito.any())).thenReturn(pessoa);

		var pessoaCriada = pessoaService.criarPessoa(pessoa);

		Assertions.assertEquals("Maria Santos", pessoaCriada.getNome());
		Assertions.assertEquals(dataNascimento, pessoaCriada.getDataNascimento());

	}

	@Test
	void deveBuscarPessoa_QuandoEnviarId() throws Exception {

		var pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Felipe Massa");
		Date dataNascimento = Date.from(Instant.parse("1988-01-19T00:00:00.000+00:00"));
		pessoa.setDataNascimento(dataNascimento);

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.of(pessoa));

		var pessoaRetornada = pessoaService.buscarOuFalhar(1L);
		Assertions.assertEquals("Felipe Massa", pessoaRetornada.getNome());
		Assertions.assertEquals(dataNascimento, pessoaRetornada.getDataNascimento());

	}

	@Test
	void deveFalhar_QuandoBuscarPessoaInexistente() throws Exception {

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		Assertions.assertThrows(PessoaNaoEncontradoException.class, () -> {
			pessoaService.buscarOuFalhar(333L);
		});

	}

	@Test
	void deveListarPessoas_QuandoListarPessoas() throws Exception {

		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		pessoas.add(new Pessoa());
		pessoas.add(new Pessoa());
		pessoas.add(new Pessoa());

		Mockito.when(pessoaRepository.findAll()).thenReturn(pessoas);
		List<Pessoa> pessoasListadas = pessoaService.listar();

		Assertions.assertEquals(3, pessoasListadas.size());
		Assertions.assertNotNull(pessoasListadas);
		Assertions.assertEquals(pessoas, pessoasListadas);
	}

	@Test
	void deveListarEndereco_QuandoListarEnderecoPorPessoaId() throws Exception {

		var pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Brian Herta");
		Date dataNascimento = Date.from(Instant.parse("1978-07-11T00:00:00.000+00:00"));
		pessoa.setDataNascimento(dataNascimento);

		var endereco = new Endereco();
		endereco.setId(1L);
		endereco.setCep("02040033");
		endereco.setCidade("curitiba");
		endereco.setLogradouro("rua de teste");
		endereco.setNumero("22");
		endereco.setPessoa(pessoa);

		var endereco2 = new Endereco();
		endereco2.setId(2L);
		endereco2.setCep("02233400");
		endereco2.setCidade("curitiba");
		endereco2.setLogradouro("avenida de teste");
		endereco2.setNumero("11");
		endereco2.setPessoa(pessoa);

		List<Endereco> enderecos = new ArrayList<Endereco>();
		enderecos.add(endereco);
		enderecos.add(endereco2);
		Mockito.when(pessoaRepository.findEnderecosByPessoaId(Mockito.any())).thenReturn(enderecos);

		List<Endereco> enderecosRetornados = pessoaService.buscarEnderecosPorIdPessoa(pessoa.getId());

		Assertions.assertEquals(2, enderecosRetornados.size());
		Assertions.assertNotNull(enderecosRetornados);
		Assertions.assertEquals(enderecos, enderecosRetornados);

	}

	@Test
	void deveAlterarIdEnderecoPrincipal_QuandoAlterarEnderecoPrincipal() throws Exception {
		var pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Ana Paula");
		Date dataNascimento = Date.from(Instant.parse("1978-07-11T00:00:00.000+00:00"));
		pessoa.setDataNascimento(dataNascimento);

		var endereco = new Endereco();
		endereco.setId(1L);
		endereco.setCep("02040033");
		endereco.setCidade("curitiba");
		endereco.setLogradouro("rua de teste");
		endereco.setNumero("22");
		endereco.setPessoa(pessoa);

		pessoa.setEnderecoPrincipalId(endereco.getId());

		var endereco2 = new Endereco();
		endereco2.setId(2L);
		endereco2.setCep("02233400");
		endereco2.setCidade("curitiba");
		endereco2.setLogradouro("avenida de teste");
		endereco2.setNumero("11");
		endereco2.setPessoa(pessoa);

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.of( pessoa));
		Mockito.when(pessoaRepository.findEnderecoPrincipalByPessoaId(Mockito.any())).thenReturn(endereco2);
		pessoaService.setEnderecoPrincipal(pessoa.getId(), endereco2.getId());

		Endereco enderecoPrincipalAtualizado = pessoaService.buscarEnderecosEnderecoPrincipal(pessoa.getId());
		
		Assertions.assertNotNull(enderecoPrincipalAtualizado);
		Assertions.assertEquals(2, enderecoPrincipalAtualizado.getId());

	}

}
