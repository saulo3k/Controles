package br.com.rexapps.controles.repository;

import br.com.rexapps.controles.domain.Pedido;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Pedido entity.
 */
public interface PedidoRepository extends JpaRepository<Pedido,Long> {

    @Query("select pedido from Pedido pedido where pedido.user.login = ?#{principal.username}")
    List<Pedido> findByUserIsCurrentUser();

}
