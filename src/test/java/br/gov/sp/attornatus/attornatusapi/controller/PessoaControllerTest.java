package br.gov.sp.attornatus.attornatusapi.controller;
 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Date;

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

		this.mockMvc.perform(post("/pessoas").contentType(MediaType.APPLICATION_JSON).content(
				"{\"nome\": \"Joao da Silva\",\"dataNascimento\": \"2013-06-09T00:00:00.000+00:00\" }")
				.accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.nome").value("Joao da Silva")).andExpect(
				 content().json("{\r\n"
						+ "    \"id\": 1,\r\n"
						+ "    \"nome\": \"Joao da Silva\",\r\n"
						+ "    \"dataNascimento\": \"2013-06-09T00:00:00.000+00:00\"\r\n"
						+ "}", true));
	}

	@Test
	void deveRetornarBadRequest_QuandoNaoEnviarPessoaNoCorpoDaRequisicao() throws Exception {
 
		this.mockMvc.perform(post("/pessoas").contentType(MediaType.APPLICATION_JSON)
				.content("")
				.accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isBadRequest());
	}
	
	 

	 
}
