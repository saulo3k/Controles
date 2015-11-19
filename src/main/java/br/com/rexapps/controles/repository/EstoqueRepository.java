package br.com.rexapps.controles.repository;

import br.com.rexapps.controles.domain.Estoque;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Estoque entity.
 */
public interface EstoqueRepository extends JpaRepository<Estoque,Long> {

    @Query("select estoque from Estoque estoque where estoque.estoque_user.login = ?#{principal.username}")
    List<Estoque> findByEstoque_userIsCurrentUser();

}
