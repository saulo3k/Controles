package br.com.rexapps.controles.repository.search;

import br.com.rexapps.controles.domain.Estoque;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Estoque entity.
 */
public interface EstoqueSearchRepository extends ElasticsearchRepository<Estoque, Long> {
}
