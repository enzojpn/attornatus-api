package br.gov.sp.attornatus.attornatusapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

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
						+ "    \"dataNascimento\": \"2013-06-09T00:00:00.000+00:00\"\r\n" + "}", true));
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
						+ "    \"dataNascimento\": \"1978-02-11T00:00:00.000+00:00\"\r\n" + "}", true));
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

		this.mockMvc.perform(get("/pessoas/1").contentType(MediaType.APPLICATION_JSON) 

		).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Claire Wiliams"))
		.andExpect(content().json("{\r\n" + "    \"id\": 1,\r\n" + "    \"nome\": \"Claire Wiliams\",\r\n"
				+ "    \"dataNascimento\": \"1958-10-08T00:00:00.000+00:00\"\r\n" + "}", true));
	}

	@Test
	void deveRetornarPessoaNotFound_QuandoBuscarPessoaComIdNaoExistente() throws Exception {

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.empty());

		this.mockMvc.perform(get("/pessoas/1").contentType(MediaType.APPLICATION_JSON) 

		).andDo(print()).andExpect(status().isNotFound());
	}

}
