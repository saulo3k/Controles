package br.com.rexapps.controles.repository;

import br.com.rexapps.controles.domain.Pedido;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Pedido entity.
 */
public interface PedidoRepository extends JpaRepository<Pedido,Long> {

    @Query("select pedido from Pedido pedido where pedido.user_pedido.login = ?#{principal.username}")
    List<Pedido> findByUser_pedidoIsCurrentUser();

    @Query("select distinct pedido from Pedido pedido left join fetch pedido.produto_has_pedidos")
    List<Pedido> findAllWithEagerRelationships();

    @Query("select pedido from Pedido pedido left join fetch pedido.produto_has_pedidos where pedido.id =:id")
    Pedido findOneWithEagerRelationships(@Param("id") Long id);

}
