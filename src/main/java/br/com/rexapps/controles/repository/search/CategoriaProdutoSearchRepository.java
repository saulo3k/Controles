package br.com.rexapps.controles.repository.search;

import br.com.rexapps.controles.domain.CategoriaProduto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CategoriaProduto entity.
 */
public interface CategoriaProdutoSearchRepository extends ElasticsearchRepository<CategoriaProduto, Long> {
}
