package br.com.rexapps.controles.repository;

import br.com.rexapps.controles.domain.Estoque;
import br.com.rexapps.controles.domain.Pedido;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Estoque entity.
 */
public interface EstoqueRepository extends JpaRepository<Estoque,Long> {

    @Query("select estoque from Estoque estoque where estoque.estoque_user.login = ?#{principal.username}")
    List<Estoque> findByEstoque_userIsCurrentUser();
    
//    @Query("select estoque from Estoque estoque left join fetch produto")
//    Pedido findAllWithEagerRelationships(@Param("id") Long id);

}
