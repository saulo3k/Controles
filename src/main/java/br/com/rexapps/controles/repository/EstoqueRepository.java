package br.com.rexapps.controles.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.rexapps.controles.domain.Estoque;

/**
 * Spring Data JPA repository for the Estoque entity.
 */
public interface EstoqueRepository extends JpaRepository<Estoque,Long> {

    @Query("select estoque from Estoque estoque where estoque.estoque_user.login = ?#{principal.username}")
    List<Estoque> findByEstoque_userIsCurrentUser();
    
    @Query("select estoque from Estoque estoque where estoque.estoque_produto.id = ?")
    List<Estoque> findByProduto(Long id);
    
    @Query("select estoque from Estoque estoque ORDER BY estoque.dataAtual DESC")
    Page<Estoque> findAllOrderByDate(Pageable pageable);

}
