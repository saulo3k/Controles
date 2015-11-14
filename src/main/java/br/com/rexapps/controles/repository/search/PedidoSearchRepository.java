package br.com.rexapps.controles.repository.search;

import br.com.rexapps.controles.domain.Pedido;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Pedido entity.
 */
public interface PedidoSearchRepository extends ElasticsearchRepository<Pedido, Long> {
}
