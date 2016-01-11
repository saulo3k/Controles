package br.com.rexapps.controles.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.rexapps.controles.domain.Produto;

/**
 * Spring Data JPA repository for the Produto entity.
 */
public interface ProdutoRepository extends JpaRepository<Produto,Long> {

    @Query("select produto from Produto produto where produto.user.login = ?#{principal.username}")
    List<Produto> findByUserIsCurrentUser();
    
    @Query("select sum(produto.estoque) from Produto produto")
    Long countProdutos();
    
    @Query("select produto from Produto produto WHERE produto.removidoLogicamente = null order by produto.nome")
    Page<Produto> findAllOrderByName(Pageable pageable);

}
