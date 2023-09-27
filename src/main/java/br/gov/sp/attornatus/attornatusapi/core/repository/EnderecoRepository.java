package br.gov.sp.attornatus.attornatusapi.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import br.gov.sp.attornatus.attornatusapi.core.model.Endereco; 

public interface EnderecoRepository extends JpaRepository<Endereco, Long>{
  

    List<Endereco> findEnderecoByPessoaId(Long pessoaId);
 
}
