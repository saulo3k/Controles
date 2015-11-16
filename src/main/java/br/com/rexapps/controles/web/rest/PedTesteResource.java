package br.com.rexapps.controles.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rexapps.controles.domain.PedTeste;
import br.com.rexapps.controles.repository.PedTesteRepository;
import br.com.rexapps.controles.repository.search.PedTesteSearchRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PedTeste.
 */
@RestController
@RequestMapping("/api")
public class PedTesteResource {

    private final Logger log = LoggerFactory.getLogger(PedTesteResource.class);

    @Inject
    private PedTesteRepository pedTesteRepository;

    @Inject
    private PedTesteSearchRepository pedTesteSearchRepository;

    /**
     * POST  /pedTestes -> Create a new pedTeste.
     */
    @RequestMapping(value = "/pedTestes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PedTeste> createPedTeste(@RequestBody PedTeste pedTeste) throws URISyntaxException {
        log.debug("REST request to save PedTeste : {}", pedTeste);
        if (pedTeste.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new pedTeste cannot already have an ID").body(null);
        }
        PedTeste result = pedTesteRepository.save(pedTeste);
        pedTesteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/pedTestes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("pedTeste", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pedTestes -> Updates an existing pedTeste.
     */
    @RequestMapping(value = "/pedTestes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PedTeste> updatePedTeste(@RequestBody PedTeste pedTeste) throws URISyntaxException {
        log.debug("REST request to update PedTeste : {}", pedTeste);
        if (pedTeste.getId() == null) {
            return createPedTeste(pedTeste);
        }
        PedTeste result = pedTesteRepository.save(pedTeste);
        pedTesteSearchRepository.save(pedTeste);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("pedTeste", pedTeste.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pedTestes -> get all the pedTestes.
     */
    @RequestMapping(value = "/pedTestes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PedTeste>> getAllPedTestes(Pageable pageable)
        throws URISyntaxException {
        Page<PedTeste> page = pedTesteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pedTestes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pedTestes/:id -> get the "id" pedTeste.
     */
    @RequestMapping(value = "/pedTestes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PedTeste> getPedTeste(@PathVariable Long id) {
        log.debug("REST request to get PedTeste : {}", id);
        return Optional.ofNullable(pedTesteRepository.findOneWithEagerRelationships(id))
            .map(pedTeste -> new ResponseEntity<>(
                pedTeste,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pedTestes/:id -> delete the "id" pedTeste.
     */
    @RequestMapping(value = "/pedTestes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePedTeste(@PathVariable Long id) {
        log.debug("REST request to delete PedTeste : {}", id);
        pedTesteRepository.delete(id);
        pedTesteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("pedTeste", id.toString())).build();
    }

    /**
     * SEARCH  /_search/pedTestes/:query -> search for the pedTeste corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/pedTestes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PedTeste> searchPedTestes(@PathVariable String query) {
        return StreamSupport
            .stream(pedTesteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
