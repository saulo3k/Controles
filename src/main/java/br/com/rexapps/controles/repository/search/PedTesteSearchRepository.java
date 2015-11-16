package br.com.rexapps.controles.repository.search;

import br.com.rexapps.controles.domain.PedTeste;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PedTeste entity.
 */
public interface PedTesteSearchRepository extends ElasticsearchRepository<PedTeste, Long> {
}
