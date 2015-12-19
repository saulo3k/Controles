package br.com.rexapps.controles.repository;

import br.com.rexapps.controles.domain.Produto;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Produto entity.
 */
public interface ProdutoRepository extends JpaRepository<Produto,Long> {

    @Query("select produto from Produto produto where produto.user.login = ?#{principal.username}")
    List<Produto> findByUserIsCurrentUser();
    
    @Query("select sum(produto.estoque) from Produto produto")
    Long countProdutos();

}
