package br.gov.sp.attornatus.attornatusapi.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.sp.attornatus.attornatusapi.core.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
