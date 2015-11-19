package br.com.rexapps.controles.repository.search;

import br.com.rexapps.controles.domain.Ordem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Ordem entity.
 */
public interface OrdemSearchRepository extends ElasticsearchRepository<Ordem, Long> {
}
