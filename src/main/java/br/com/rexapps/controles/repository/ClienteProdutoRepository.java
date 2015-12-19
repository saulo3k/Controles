package br.com.rexapps.controles.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.rexapps.controles.domain.ClienteProduto;

/**
 * Spring Data JPA repository for the ClienteProduto entity.
 */
public interface ClienteProdutoRepository extends JpaRepository<ClienteProduto,Long> {

    @Query("select clienteProduto from ClienteProduto clienteProduto where clienteProduto.cliente.id = :id")
    List<ClienteProduto> findAllbyCliente(@Param("id") Long id);
}
