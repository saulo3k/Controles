package br.com.rexapps.controles.repository;

import br.com.rexapps.controles.domain.Ordem;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Ordem entity.
 */
public interface OrdemRepository extends JpaRepository<Ordem,Long> {

    @Query("select ordem from Ordem ordem where ordem.ordem_user.login = ?#{principal.username}")
    List<Ordem> findByOrdem_userIsCurrentUser();

    @Query("select distinct ordem from Ordem ordem left join fetch ordem.ordem_produtos")
    List<Ordem> findAllWithEagerRelationships();

    @Query("select ordem from Ordem ordem left join fetch ordem.ordem_produtos where ordem.id =:id")
    Ordem findOneWithEagerRelationships(@Param("id") Long id);

}
