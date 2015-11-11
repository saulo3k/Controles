package br.com.rexapps.controles.repository.search;

import br.com.rexapps.controles.domain.Email;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Email entity.
 */
public interface EmailSearchRepository extends ElasticsearchRepository<Email, Long> {
}
