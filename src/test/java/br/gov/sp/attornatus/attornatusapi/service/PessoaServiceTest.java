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
	
 

}
