package br.com.rexapps.controles.repository;

import br.com.rexapps.controles.domain.CategoriaProduto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CategoriaProduto entity.
 */
public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto,Long> {

}
