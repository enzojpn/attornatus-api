package br.gov.sp.attornatus.attornatusapi.core.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PessoaController {

	@GetMapping("/oi")
	public String oi() {
		return "oi";
	}
	
	
}
