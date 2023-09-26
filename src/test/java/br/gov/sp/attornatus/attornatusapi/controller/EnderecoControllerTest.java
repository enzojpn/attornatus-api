package br.gov.sp.attornatus.attornatusapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import br.gov.sp.attornatus.attornatusapi.core.model.Endereco;
import br.gov.sp.attornatus.attornatusapi.core.model.Pessoa;
import br.gov.sp.attornatus.attornatusapi.core.repository.EnderecoRepository;
import br.gov.sp.attornatus.attornatusapi.core.repository.PessoaRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class EnderecoControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	PessoaRepository pessoaRepository;

	@MockBean
	EnderecoRepository enderecoRepository;

	@Test
	void deveRetornarCreated_QuandoGravarEndereco() throws Exception {

		var pessoa = new Pessoa();
		pessoa.setId(1L);
		pessoa.setNome("Nigel Mansell");

		Date dataNascimento = Date.from(Instant.parse("2013-06-09T00:00:00.000+00:00"));
		pessoa.setDataNascimento(dataNascimento);
  
		var endereco = new Endereco();
		endereco.setId(1L);
		endereco.setCep("02040033");
		endereco.setCidade("curitiba");
		endereco.setLogradouro("rua de teste");
		endereco.setNumero("22");
		endereco.setPessoa(pessoa);

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.of(pessoa) );
		Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(endereco );
		
		
		this.mockMvc.perform(post("/enderecos").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n"
						+ "  \"logradouro\": \"rua de teste\",\r\n"
						+ "  \"cidade\": \"curitiba\",\r\n"
						+ "  \"numero\": \"22\",\r\n"
						+ "  \"cep\": \"02040033\"  ,\r\n"
						+ "  \"pessoa\" : {\r\n"
						+ "      \"id\" : 1\r\n"
						+ "  }\r\n"
						+ " \r\n"
						+ "}")
				.accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.pessoa.nome").value("Nigel Mansell"))
				.andExpect(content().json("{\r\n"
						+ "    \"id\": 1,\r\n"
						+ "    \"pessoa\": {\r\n"
						+ "        \"id\": 1,\r\n"
						+ "        \"nome\": \"Nigel Mansell\",\r\n"
						+ "        \"dataNascimento\": \"2013-06-09T00:00:00.000+00:00\"\r\n"
						+ "    },\r\n"
						+ "    \"logradouro\": \"rua de teste\",\r\n"
						+ "    \"cep\": \"02040033\",\r\n"
						+ "    \"numero\": \"22\",\r\n"
						+ "    \"cidade\": \"curitiba\"\r\n"
						+ "}", true));
	}

	@Test
	void deveRetornarBadRequest_QuandoNaoEnviarEnderecoNoCorpoDaRequisicao() throws Exception {

		this.mockMvc.perform(
				post("/enderecos").contentType(MediaType.APPLICATION_JSON).content("").accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isBadRequest());
	}
 
 

}
