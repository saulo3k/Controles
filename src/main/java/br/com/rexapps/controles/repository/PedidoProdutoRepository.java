package br.com.rexapps.controles.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.rexapps.controles.domain.ProdutosPedidos;

/**
 * Spring Data JPA repository for the Pedido entity.
 */
public interface PedidoProdutoRepository extends JpaRepository<ProdutosPedidos,Long> {

    @Query("select produtosPedidos from ProdutosPedidos produtosPedidos where produtosPedidos.pedido.id= :id")
    Set<ProdutosPedidos> findByIdPedidos(@Param("id") Long id);
    
    @Query("select produtosPedidos from ProdutosPedidos produtosPedidos where produtosPedidos.produto.id= :id")
    Set<ProdutosPedidos> findByIdProduto(@Param("id") Long id);
        
}
