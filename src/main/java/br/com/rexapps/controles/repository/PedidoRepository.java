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
    
    @Query("select pedido from Pedido pedido where pedido.pedidoModelo = false ORDER BY pedido.id DESC")
    Page<Pedido> findAllWithOutPedidoModelo(Pageable pageable);
    
    @Query("select pedido from Pedido pedido where pedido.statusPedido = 4")
    Page<Pedido> findAllSeparacao(Pageable pageable);
    
    @Query("select pedido from Pedido pedido where pedido.statusPedido in (5,7)")
    Page<Pedido> findAllEntregas(Pageable pageable);
    
    @Query("select pedido from Pedido pedido where pedido.pedidoModelo = true")
    Page<Pedido> findAllPedidosModelos(Pageable pageable);
    
    @Query("select pedido from Pedido pedido where pedido.pedidoModelo = true")
    List<Pedido> findAllPedidosModelosAutomaticos();
    
    @Query("select distinct pedido from Pedido pedido left join fetch pedido.diasSemana where pedido.id =:id")
    Pedido findOneWithEagerRelationshipsDaysOfWeek(@Param("id") Long id);

    @Query("select pedido from Pedido pedido where pedido.statusPedido = 2")
    Page<Pedido> findAllEqPedido(Pageable pageable);
    
    @Query("select count(pedido) from Pedido pedido where pedido.statusPedido = 4")
    Long countPedidosSeparacao();
    
    @Query("select count(pedido) from Pedido pedido where pedido.statusPedido = 5")
    Long countPedidosEntrega();

}
