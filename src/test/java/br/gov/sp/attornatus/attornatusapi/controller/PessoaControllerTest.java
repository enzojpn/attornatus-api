package br.gov.sp.attornatus.attornatusapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.gov.sp.attornatus.attornatusapi.core.model.Pessoa;
import br.gov.sp.attornatus.attornatusapi.core.repository.PessoaRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class PessoaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	PessoaRepository pessoaRepository;

	@Test
	void deveRetornarCreated_QuandoGravarPessoa() throws Exception {

		var pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Joao da Silva");

		Date dataNascimento = Date.from(Instant.parse("2013-06-09T00:00:00.000+00:00"));

		pessoa.setDataNascimento(dataNascimento);

		Mockito.when(pessoaRepository.save(Mockito.any())).thenReturn(pessoa);

		this.mockMvc.perform(post("/pessoas").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\": \"Joao da Silva\",\"dataNascimento\": \"2013-06-09T00:00:00.000+00:00\" }")
				.accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.nome").value("Joao da Silva"))
				.andExpect(content().json("{\r\n" + "    \"id\": 1,\r\n" + "    \"nome\": \"Joao da Silva\",\r\n"
						+ "    \"dataNascimento\": \"2013-06-09T00:00:00.000+00:00\"\r\n"
						+ ",\"enderecoPrincipalId\":null}", true));
	}

	@Test
	void deveRetornarBadRequest_QuandoNaoEnviarPessoaNoCorpoDaRequisicao() throws Exception {

		this.mockMvc.perform(
				post("/pessoas").contentType(MediaType.APPLICATION_JSON).content("").accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	void deveRetornarOk_QuandoAlterarPessoaComSucesso() throws Exception {

		var pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Alain Prost");
		Date dataNascimento = Date.from(Instant.parse("1978-02-11T00:00:00.000+00:00"));
		pessoa.setDataNascimento(dataNascimento);

		Mockito.when(pessoaRepository.save(Mockito.any())).thenReturn(pessoa);
		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.of(pessoa));

		this.mockMvc.perform(put("/pessoas/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\": \"Jean Alesi\",\"dataNascimento\": \"1978-02-11T00:00:00.000+00:00\" }")
				.accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Jean Alesi"))
				.andExpect(content().json("{\r\n" + "    \"id\": 1,\r\n" + "    \"nome\": \"Jean Alesi\",\r\n"
						+ "    \"dataNascimento\": \"1978-02-11T00:00:00.000+00:00\"\r\n"
						+ ",\"enderecoPrincipalId\":null}", true));
	}

	@Test
	void deveRetornarPessoaNotFound_QuandoAlterarPessoaComIdNaoExistente() throws Exception {

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.empty());

		this.mockMvc.perform(put("/pessoas/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\": \"Jean Alesi\",\"dataNascimento\": \"1978-02-11T00:00:00.000+00:00\" }")
				.accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	void deveRetornarOk_QuandoBuscarPessoaComIdExistente() throws Exception {
		var pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Claire Wiliams");
		Date dataNascimento = Date.from(Instant.parse("1958-10-08T00:00:00.000+00:00"));
		pessoa.setDataNascimento(dataNascimento);

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.of(pessoa));

		this.mockMvc.perform(get("/pessoas/1").accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Claire Wiliams"))
				.andExpect(content().json("{\r\n" + "    \"id\": 1,\r\n" + "    \"nome\": \"Claire Wiliams\",\r\n"
						+ "    \"dataNascimento\": \"1958-10-08T00:00:00.000+00:00\"\r\n"
						+ ",\"enderecoPrincipalId\":null}", true));
	}

	@Test
	void deveRetornarPessoaNotFound_QuandoBuscarPessoaComIdNaoExistente() throws Exception {

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.empty());

		this.mockMvc.perform(get("/pessoas/1").accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	void deveRetornarOk_QuandoListarPessoas() throws Exception {

		List<Pessoa> pessoas = new ArrayList<Pessoa>();
		var pessoa1 = new Pessoa();
		var pessoa2 = new Pessoa();
		var pessoa3 = new Pessoa();

		pessoa1.setId(1L);
		pessoa2.setId(2L);
		pessoa3.setId(3L);

		pessoa1.setNome("Nuno");
		pessoa2.setNome("Alice");
		pessoa3.setNome("Yuri");

		Date dataNascimento = Date.from(Instant.parse("1958-10-08T00:00:00.000+00:00"));
		pessoa1.setDataNascimento(dataNascimento);
		pessoa2.setDataNascimento(dataNascimento);
		pessoa3.setDataNascimento(dataNascimento);

		pessoas.add(pessoa1);
		pessoas.add(pessoa2);
		pessoas.add(pessoa3);

		Mockito.when(pessoaRepository.findAll()).thenReturn(pessoas);

		this.mockMvc.perform(get("/pessoas").accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(3)))
				.andExpect(jsonPath("$[0].id").value(1)).andExpect(jsonPath("$[0].nome").value("Nuno"))
				.andExpect(jsonPath("$[0].dataNascimento").value("1958-10-08T00:00:00.000+00:00"))
				.andExpect(jsonPath("$[1].id").value(2)).andExpect(jsonPath("$[1].nome").value("Alice"))
				.andExpect(jsonPath("$[1].dataNascimento").value("1958-10-08T00:00:00.000+00:00"))
				.andExpect(jsonPath("$[2].id").value(3)).andExpect(jsonPath("$[2].nome").value("Yuri"))
				.andExpect(jsonPath("$[2].dataNascimento").value("1958-10-08T00:00:00.000+00:00"));
	}

	@Test
	void deveRetornarNotFound_QuandoAlterarEnderecoPrincipalDePessoaInexistente() throws Exception {

		this.mockMvc.perform(put("/pessoas/1/endereco/2/principal").contentType(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	void deveRetornarOk_QuandoAlterarEnderecoPrincipal() throws Exception {

		var pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Claire Wiliams");
		Date dataNascimento = Date.from(Instant.parse("1958-10-08T00:00:00.000+00:00"));
		pessoa.setDataNascimento(dataNascimento);

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.of(pessoa));
		
		this.mockMvc.perform(put("/pessoas/1/endereco/2/principal").contentType(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isOk());
	}

}
