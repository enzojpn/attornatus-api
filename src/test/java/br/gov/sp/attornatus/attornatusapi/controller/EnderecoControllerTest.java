package br.gov.sp.attornatus.attornatusapi.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.of(pessoa));
		Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(endereco);

		this.mockMvc.perform(post("/enderecos").contentType(MediaType.APPLICATION_JSON)
				.content("{\r\n" + "  \"logradouro\": \"rua de teste\",\r\n" + "  \"cidade\": \"curitiba\",\r\n"
						+ "  \"numero\": \"22\",\r\n" + "  \"cep\": \"02040033\"  ,\r\n" + "  \"pessoa\" : {\r\n"
						+ "      \"id\" : 1\r\n" + "  }\r\n" + " \r\n" + "}")
				.accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.pessoa.nome").value("Nigel Mansell"))
				.andExpect(content().json("{\r\n" + "    \"id\": 1,\r\n" + "    \"pessoa\": {\r\n"
						+ "        \"id\": 1,\r\n" + "        \"nome\": \"Nigel Mansell\",\r\n"
						+ "        \"dataNascimento\": \"2013-06-09T00:00:00.000+00:00\",\r\n"
						+ "			\"enderecoPrincipalId\":null" + "    },\r\n"
						+ "    \"logradouro\": \"rua de teste\",\r\n" + "    \"cep\": \"02040033\",\r\n"
						+ "    \"numero\": \"22\",\r\n" + "    \"cidade\": \"curitiba\"\r\n" + "}", true));
	}

	@Test
	void deveRetornarBadRequest_QuandoNaoEnviarEnderecoNoCorpoDaRequisicao() throws Exception {

		this.mockMvc.perform(post("/enderecos").contentType(MediaType.APPLICATION_JSON).content("")
				.accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	void deveRetornarOk_QuandoListarEndereco() throws Exception {

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

		var endereco2 = new Endereco();
		endereco2.setId(2L);
		endereco2.setCep("8602022444");
		endereco2.setCidade("londrina");
		endereco2.setLogradouro("avenida maringa");
		endereco2.setNumero("224");
		endereco2.setPessoa(pessoa);

		List<Endereco> enderecos = new ArrayList<Endereco>();
		enderecos.add(endereco);
		enderecos.add(endereco2);

		Mockito.when(enderecoRepository.findAll()).thenReturn(enderecos);

		 this.mockMvc.perform(
				get("/enderecos").accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isOk())	
	   .andExpect(jsonPath("$.length()", is(2)))
		.andExpect(jsonPath("$[0].pessoa.id").value("1"))
		.andExpect(jsonPath("$[0].pessoa.nome").value("Nigel Mansell"))
		.andExpect(jsonPath("$[0].pessoa.dataNascimento").value("2013-06-09T00:00:00.000+00:00"))
		.andExpect(jsonPath("$[0].logradouro").value("rua de teste"))
		.andExpect(jsonPath("$[0].cep").value("02040033")).andExpect(jsonPath("$[0].numero").value("22"))
		.andExpect(jsonPath("$[0].cidade").value("curitiba"));
		
	 
	}

	@Test
	void deveRetornarEnderecos_QuandoEnviarEnderecoPorPessoaId() throws Exception {

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

		List<Endereco> enderecos = new ArrayList<Endereco>();
		enderecos.add(endereco);

		Mockito.when(enderecoRepository.findEnderecoByPessoaId(Mockito.any())).thenReturn(enderecos);

		this.mockMvc.perform(get("/enderecos/por-pessoa-id/1").accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", is(1)))
				.andExpect(jsonPath("$[0].pessoa.id").value("1"))
				.andExpect(jsonPath("$[0].pessoa.nome").value("Nigel Mansell"))
				.andExpect(jsonPath("$[0].pessoa.dataNascimento").value("2013-06-09T00:00:00.000+00:00"))
				.andExpect(jsonPath("$[0].logradouro").value("rua de teste"))
				.andExpect(jsonPath("$[0].cep").value("02040033")).andExpect(jsonPath("$[0].numero").value("22"))
				.andExpect(jsonPath("$[0].cidade").value("curitiba"));
	}

}
