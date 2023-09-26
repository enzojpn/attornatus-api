package br.gov.sp.attornatus.attornatusapi.service;

import java.time.Instant;
import java.util.Date;
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
import br.gov.sp.attornatus.attornatusapi.core.repository.EnderecoRepository;
import br.gov.sp.attornatus.attornatusapi.core.repository.PessoaRepository;
import br.gov.sp.attornatus.attornatusapi.core.service.EnderecoService;

@SpringBootTest
@AutoConfigureMockMvc
public class EnderecoServiceTest {

	@MockBean
	PessoaRepository pessoaRepository;

	@MockBean
	EnderecoRepository enderecoRepository;

	@Autowired
	EnderecoService enderecoService;

	@Test
	void deveRetornarEndereco_QuandoCriarEndereco() throws Exception {

		var pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Silvio Santos");
		Date dataNascimento = Date.from(Instant.parse("2003-11-19T00:00:00.000+00:00"));
		pessoa.setDataNascimento(dataNascimento);

		var endereco = new Endereco();
		endereco.setId(1L);
		endereco.setCep("02040033");
		endereco.setCidade("curitiba");
		endereco.setLogradouro("rua de teste");
		endereco.setNumero("22");
		endereco.setPessoa(pessoa);

		Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(endereco);

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.of(pessoa));

		var enderecoCriado = enderecoService.criarEndereco(endereco);

		Assertions.assertEquals("Silvio Santos", enderecoCriado.getPessoa().getNome());
		Assertions.assertEquals(dataNascimento, enderecoCriado.getPessoa().getDataNascimento());
		Assertions.assertEquals("02040033", enderecoCriado.getCep());
		Assertions.assertEquals("curitiba", enderecoCriado.getCidade());
		Assertions.assertEquals("22", enderecoCriado.getNumero());
		Assertions.assertEquals("rua de teste", enderecoCriado.getLogradouro());

	}

	@Test
	void deveRetornarPessoaNaoEncontradaException_QuandoEnviarEnderecoSemPessoa() throws Exception {
 
		var endereco = new Endereco();
		endereco.setId(1L);
		endereco.setCep("02040033");
		endereco.setCidade("curitiba");
		endereco.setLogradouro("rua de teste");
		endereco.setNumero("22"); 

		Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(endereco);

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.empty());


		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		Assertions.assertThrows(PessoaNaoEncontradoException.class, () -> {
			 enderecoService.criarEndereco(endereco);
		});

	}
	@Test
	void deveRetornarPessoaNaoEncontradaException_QuandoNaoEncontrarPessoa() throws Exception {
		var pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Silvio Santos");
		Date dataNascimento = Date.from(Instant.parse("2003-11-19T00:00:00.000+00:00"));
		pessoa.setDataNascimento(dataNascimento);

		var endereco = new Endereco();
		endereco.setId(1L);
		endereco.setCep("02040033");
		endereco.setCidade("curitiba");
		endereco.setLogradouro("rua de teste");
		endereco.setNumero("22");
		endereco.setPessoa(pessoa);

		Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(endereco);

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.empty());


		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.empty());
		Assertions.assertThrows(PessoaNaoEncontradoException.class, () -> {
			 enderecoService.criarEndereco(endereco);
		});

	}

}
