package br.com.rexapps.controles.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.com.rexapps.controles.domain.Pedido;
import br.com.rexapps.controles.repository.PedidoProdutoRepository;
import br.com.rexapps.controles.repository.PedidoRepository;
import br.com.rexapps.controles.repository.search.PedidoSearchRepository;
import br.com.rexapps.controles.service.PedidoService;
import br.com.rexapps.controles.web.rest.util.HeaderUtil;
import br.com.rexapps.controles.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Pedido.
 */
@RestController
@RequestMapping("/api")
public class PedidoResource {

	private final Logger log = LoggerFactory.getLogger(PedidoResource.class);

	@Inject
	private PedidoRepository pedidoRepository;

	@Inject
	private PedidoProdutoRepository pedidoProdutoRepository;

	@Inject
	private PedidoService pedidoService;

	@Inject
	private PedidoSearchRepository pedidoSearchRepository;

	/**
	 * POST /pedidos -> Create a new pedido.
	 */
	@RequestMapping(value = "/pedidos", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Pedido> createPedido(@RequestBody Pedido pedido) throws URISyntaxException {
		log.debug("REST request to save Pedido : {}", pedido);
		if (pedido.getId() != null) {
			return ResponseEntity.badRequest().header("Failure", "A new pedido cannot already have an ID").body(null);
		}
		Pedido result = pedidoService.savePedido(pedido);
		result.setProdutosPedidos(null);
		pedidoSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/pedidos/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("pedido", result.getId().toString())).body(result);
	}

	@RequestMapping(value = "/pedidosmodelo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Pedido> createPedidoModelo(@RequestBody Pedido pedido) throws URISyntaxException {
		log.debug("REST request to save Pedido : {}", pedido);
		if (pedido.getId() != null) {
			return ResponseEntity.badRequest().header("Failure", "A new pedido cannot already have an ID").body(null);
		}
		Pedido result = pedidoService.savePedidoModelo(pedido);
		result.setProdutosPedidos(null);
		pedidoSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/pedidos/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("pedido", result.getId().toString())).body(result);
	}
	

	/**
	 * PUT /pedidos -> Updates an existing pedido.
	 */
	@RequestMapping(value = "/pedidosmodelo", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Pedido> gerarPedidoAutomatico(@RequestBody Pedido pedido) throws URISyntaxException {
		log.debug("REST request to update Pedido : {}", pedido);
		if (pedido.getId() == null) {
			return createPedido(pedido);
		}
		Pedido result = pedidoService.gerarPedidoAutomatico(pedido);
		result.setProdutosPedidos(null);
		pedidoSearchRepository.save(pedido);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("pedido", pedido.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /pedidos -> Updates an existing pedido.
	 */
	@RequestMapping(value = "/pedidos", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Pedido> updatePedido(@RequestBody Pedido pedido) throws URISyntaxException {
		log.debug("REST request to update Pedido : {}", pedido);
		if (pedido.getId() == null) {
			return createPedido(pedido);
		}
		Pedido result = pedidoService.updatePedido(pedido);
		result.setProdutosPedidos(null);
		pedidoSearchRepository.save(pedido);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("pedido", pedido.getId().toString()))
				.body(result);
	}
	
	/**
	 * PUT /pedidos -> Updates an existing pedido.
	 */
	@RequestMapping(value = "/separacao", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Pedido> updateSeparacao(@RequestBody Pedido pedido) throws URISyntaxException {
		log.debug("REST request to update Pedido : {}", pedido);		
		Pedido result = pedidoService.separarPedido(pedido);		
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("pedido", pedido.getId().toString()))
				.body(result);
	}
	
	/**
	 * PUT /pedidos -> Updates an existing pedido.
	 */
	@RequestMapping(value = "/entregas", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Pedido> updateEntrega(@RequestBody Pedido pedido) throws URISyntaxException {
		log.debug("REST request to update Pedido : {}", pedido);		
		Pedido result = pedidoService.entregarPedido(pedido);		
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("pedido", pedido.getId().toString()))
				.body(result);
	}

	/**
	 * GET /pedidos -> get all the pedidos.
	 */
	@RequestMapping(value = "/pedidos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Pedido>> getAllPedidos(Pageable pageable) throws URISyntaxException {
		Page<Pedido> page = pedidoRepository.findAllWithOutPedidoModelo(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pedidos");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * GET /pedidos -> get all the pedidos.
	 */
	@RequestMapping(value = "/equalizar-pedidos", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Pedido>> getAllPedidosEqualizar(Pageable pageable) throws URISyntaxException {
		Page<Pedido> page = pedidoRepository.findAllEqPedido(pageable);
		for (Pedido pedido : page.getContent()) {
			pedido.setProdutosPedidos(pedidoProdutoRepository.findByIdPedidos(pedido.getId()));	
		}		
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/equalizar-pedidos");
		
		return new ResponseEntity<>(page.getContent() , headers, HttpStatus.OK);
	}

	/**
	 * GET /pedidos/:id -> get the "id" pedido.
	 */
	@RequestMapping(value = "/pedidos/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Pedido> getPedido(@PathVariable Long id) {
		log.debug("REST request to get Pedido : {}", id);
		Pedido pedido2 = pedidoRepository.findOneWithEagerRelationshipsDaysOfWeek(id);
		pedido2.setProdutosPedidos(pedidoProdutoRepository.findByIdPedidos(id));
		pedido2.getDiasSemana();
		return Optional.ofNullable(pedido2).map(pedido -> new ResponseEntity<>(pedido, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /pedidos/:id -> delete the "id" pedido.
	 */
	@RequestMapping(value = "/pedidos/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
		log.debug("REST request to delete Pedido : {}", id);
		pedidoRepository.delete(id);
		pedidoSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pedido", id.toString())).build();
	}

	/**
	 * DELETE /pedidos/:id -> delete the "id" pedido.
	 */
	@RequestMapping(value = "/pedidosmodelo/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deletePedidoModelo(@PathVariable Long id) {
		log.debug("REST request to delete Pedido : {}", id);
		pedidoRepository.delete(id);
		pedidoSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pedido", id.toString())).build();
	}

	@RequestMapping(value = "/separacao", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Pedido>> separacaoPedidos(Pageable pageable) throws URISyntaxException {
		Page<Pedido> page = pedidoRepository.findAllSeparacao(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pedidos");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/entregas", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Pedido>> entregasPedidos(Pageable pageable) throws URISyntaxException {
		Page<Pedido> page = pedidoRepository.findAllEntregas(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pedidos");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/pedidosmodelo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Pedido>> modeloPedidos(Pageable pageable) throws URISyntaxException {
		Page<Pedido> page = pedidoRepository.findAllPedidosModelos(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pedidos");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	/**
	 * SEARCH /_search/pedidos/:query -> search for the pedido corresponding to
	 * the query.
	 */
	@RequestMapping(value = "/_search/pedidos/{query}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Pedido> searchPedidos(@PathVariable String query) {
		return StreamSupport.stream(pedidoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}

}