package br.com.rexapps.controles.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rexapps.controles.domain.Cliente;
import br.com.rexapps.controles.repository.ClienteRepository;
import br.com.rexapps.controles.repository.search.ClienteSearchRepository;
import br.com.rexapps.controles.web.rest.util.HeaderUtil;
import br.com.rexapps.controles.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Cliente.
 */
@RestController
@RequestMapping("/api")
public class ClienteResource {

    private final Logger log = LoggerFactory.getLogger(ClienteResource.class);

    @Inject
    private ClienteRepository clienteRepository;

    @Inject
    private ClienteSearchRepository clienteSearchRepository;

    /**
     * POST  /clientes -> Create a new cliente.
     */
    @RequestMapping(value = "/clientes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cliente> createCliente(@Valid @RequestBody Cliente cliente) throws URISyntaxException {
        log.debug("REST request to save Cliente : {}", cliente);
        if (cliente.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new cliente cannot already have an ID").body(null);
        }
        Cliente result = clienteRepository.save(cliente);
        clienteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/clientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cliente", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clientes -> Updates an existing cliente.
     */
    @RequestMapping(value = "/clientes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cliente> updateCliente(@Valid @RequestBody Cliente cliente) throws URISyntaxException {
        log.debug("REST request to update Cliente : {}", cliente);
        if (cliente.getId() == null) {
            return createCliente(cliente);
        }
        Cliente result = clienteRepository.save(cliente);
        clienteSearchRepository.save(cliente);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cliente", cliente.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clientes -> get all the clientes.
     */
    @RequestMapping(value = "/clientes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Cliente>> getAllClientes(Pageable pageable)
        throws URISyntaxException {
        Page<Cliente> page = clienteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clientes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /clientes/:id -> get the "id" cliente.
     */
    @RequestMapping(value = "/clientes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cliente> getCliente(@PathVariable Long id) {
        log.debug("REST request to get Cliente : {}", id);
        return Optional.ofNullable(clienteRepository.findOne(id))
            .map(cliente -> new ResponseEntity<>(
                cliente,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /clientes/:id -> delete the "id" cliente.
     */
    @RequestMapping(value = "/clientes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        log.debug("REST request to delete Cliente : {}", id);
        clienteRepository.delete(id);
        clienteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cliente", id.toString())).build();
    }

    /**
     * SEARCH  /_search/clientes/:query -> search for the cliente corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/clientes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Cliente> searchClientes(@PathVariable String query) {
        return StreamSupport
            .stream(clienteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
