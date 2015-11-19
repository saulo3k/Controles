package br.com.rexapps.controles.repository;

import br.com.rexapps.controles.domain.Pedido;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Pedido entity.
 */
public interface PedidoRepository extends JpaRepository<Pedido,Long> {

    @Query("select pedido from Pedido pedido where pedido.user_pedido.login = ?#{principal.username}")
    List<Pedido> findByUser_pedidoIsCurrentUser();

    @Query("select distinct pedido from Pedido pedido left join fetch pedido.produtosPedidos")
    List<Pedido> findAllWithEagerRelationships();

    @Query("select distinct pedido from Pedido pedido left join fetch pedido.produtosPedidos where pedido.id =:id")
    Pedido findOneWithEagerRelationships(@Param("id") Long id);
    
    @Query("select pedido from Pedido pedido where pedido.statusPedido in(4,5)")
    Page<Pedido> findAllSeparacao(Pageable pageable);
    
    @Query("select pedido from Pedido pedido where pedido.statusPedido in(6)")
    Page<Pedido> findAllEntregas(Pageable pageable);

}
