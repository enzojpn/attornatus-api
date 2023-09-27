package br.gov.sp.attornatus.attornatusapi.core.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.gov.sp.attornatus.attornatusapi.core.model.Endereco;
import br.gov.sp.attornatus.attornatusapi.core.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	@Query("SELECT p.endereco FROM Pessoa p WHERE p.id = :pessoaId")
	List<Endereco> findEnderecosByPessoaId(@Param("pessoaId") Long pessoaId);

	@Query("SELECT e FROM Endereco e WHERE e.id = (SELECT p.enderecoPrincipalId FROM Pessoa p WHERE p.id = :pessoaId)")
	Endereco findEnderecoPrincipalByPessoaId(Long pessoaId);

}
