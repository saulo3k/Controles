package br.com.rexapps.controles.repository;

import br.com.rexapps.controles.domain.PedTeste;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the PedTeste entity.
 */
public interface PedTesteRepository extends JpaRepository<PedTeste,Long> {

    @Query("select distinct pedTeste from PedTeste pedTeste left join fetch pedTeste.produtopeds")
    List<PedTeste> findAllWithEagerRelationships();

    @Query("select pedTeste from PedTeste pedTeste left join fetch pedTeste.produtopeds where pedTeste.id =:id")
    PedTeste findOneWithEagerRelationships(@Param("id") Long id);

}
