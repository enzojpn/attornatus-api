package br.gov.sp.attornatus.attornatusapi.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.sp.attornatus.attornatusapi.core.model.Endereco; 

public interface EnderecoRepository extends JpaRepository<Endereco, Long>{

}
