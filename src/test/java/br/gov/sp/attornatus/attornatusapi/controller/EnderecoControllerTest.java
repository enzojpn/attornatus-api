package br.gov.sp.attornatus.attornatusapi.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.gov.sp.attornatus.attornatusapi.core.model.Endereco;
import br.gov.sp.attornatus.attornatusapi.core.model.Pessoa;
import br.gov.sp.attornatus.attornatusapi.core.repository.EnderecoRepository;
import br.gov.sp.attornatus.attornatusapi.core.repository.PessoaRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class EnderecoControllerTest {

	ObjectMapper mapper;
	
	Pessoa pessoa = new Pessoa();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	PessoaRepository pessoaRepository;

	@MockBean
	EnderecoRepository enderecoRepository;

	@BeforeEach
	void metodoBeforeEach() {
		mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		Date dataNascimento = Date.from(Instant.parse("2013-06-11T00:00:00.000Z"));
		pessoa = new Pessoa(1L, "Nigel Mansell", dataNascimento, null);
	}

	@Test
	void deveRetornarCreated_QuandoGravarEndereco() throws Exception {
 
		var endereco = new Endereco(1L, pessoa, "rua de teste", "02040033", "22", "curitiba");

		Mockito.when(pessoaRepository.findById(Mockito.any())).thenReturn(Optional.of(pessoa));
		Mockito.when(enderecoRepository.save(Mockito.any())).thenReturn(endereco);

		String json = mapper.writeValueAsString(endereco);

		this.mockMvc.perform(post("/enderecos").contentType(MediaType.APPLICATION_JSON).content(json)
				.accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.pessoa.nome").value("Nigel Mansell"))
				.andExpect(content().json(json, true));
	}

	@Test
	void deveRetornarBadRequest_QuandoNaoEnviarEnderecoNoCorpoDaRequisicao() throws Exception {

		this.mockMvc.perform(post("/enderecos").contentType(MediaType.APPLICATION_JSON).content("")
				.accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	void deveRetornarOk_QuandoListarEndereco() throws Exception {
  
		var endereco = new Endereco(1L, pessoa, "rua de teste", "02040033", "22", "curitiba");
		var endereco2 = new Endereco(1L, pessoa, "avenida maringa", "8602022444", "224", "londrina");		

		List<Endereco> enderecos = new ArrayList<Endereco>();
		enderecos.add(endereco);
		enderecos.add(endereco2);

		Mockito.when(enderecoRepository.findAll()).thenReturn(enderecos);

		this.mockMvc.perform(get("/enderecos").accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(2)))
				.andExpect(jsonPath("$[0].pessoa.id").value("1"))
				.andExpect(jsonPath("$[0].pessoa.nome").value("Nigel Mansell"))
				.andExpect(jsonPath("$[0].pessoa.dataNascimento").value("2013-06-11T00:00:00.000Z"))
				.andExpect(jsonPath("$[0].logradouro").value("rua de teste"))
				.andExpect(jsonPath("$[0].cep").value("02040033")).andExpect(jsonPath("$[0].numero").value("22"))
				.andExpect(jsonPath("$[0].cidade").value("curitiba"));

	}

	@Test
	void deveRetornarEnderecos_QuandoEnviarEnderecoPorPessoaId() throws Exception {
 
		var endereco = new Endereco(1L, pessoa, "rua de teste", "02040033", "22", "curitiba");

		List<Endereco> enderecos = new ArrayList<Endereco>();
		enderecos.add(endereco);

		Mockito.when(enderecoRepository.findEnderecoByPessoaId(Mockito.any())).thenReturn(enderecos);

		this.mockMvc.perform(get("/enderecos/por-pessoa-id/1").accept(MediaType.APPLICATION_JSON)

		).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)))
				.andExpect(jsonPath("$[0].pessoa.id").value("1"))
				.andExpect(jsonPath("$[0].pessoa.nome").value("Nigel Mansell"))
				.andExpect(jsonPath("$[0].pessoa.dataNascimento").value("2013-06-11T00:00:00.000Z"))
				.andExpect(jsonPath("$[0].logradouro").value("rua de teste"))
				.andExpect(jsonPath("$[0].cep").value("02040033")).andExpect(jsonPath("$[0].numero").value("22"))
				.andExpect(jsonPath("$[0].cidade").value("curitiba"));
	}

}
