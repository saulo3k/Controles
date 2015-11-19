package br.com.rexapps.controles.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rexapps.controles.domain.Ordem;
import br.com.rexapps.controles.repository.OrdemRepository;
import br.com.rexapps.controles.repository.search.OrdemSearchRepository;
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
 * REST controller for managing Ordem.
 */
@RestController
@RequestMapping("/api")
public class OrdemResource {

    private final Logger log = LoggerFactory.getLogger(OrdemResource.class);

    @Inject
    private OrdemRepository ordemRepository;

    @Inject
    private OrdemSearchRepository ordemSearchRepository;

    /**
     * POST  /ordems -> Create a new ordem.
     */
    @RequestMapping(value = "/ordems",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ordem> createOrdem(@RequestBody Ordem ordem) throws URISyntaxException {
        log.debug("REST request to save Ordem : {}", ordem);
        if (ordem.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new ordem cannot already have an ID").body(null);
        }
        Ordem result = ordemRepository.save(ordem);
        ordemSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/ordems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ordem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ordems -> Updates an existing ordem.
     */
    @RequestMapping(value = "/ordems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ordem> updateOrdem(@RequestBody Ordem ordem) throws URISyntaxException {
        log.debug("REST request to update Ordem : {}", ordem);
        if (ordem.getId() == null) {
            return createOrdem(ordem);
        }
        Ordem result = ordemRepository.save(ordem);
        ordemSearchRepository.save(ordem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ordem", ordem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ordems -> get all the ordems.
     */
    @RequestMapping(value = "/ordems",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ordem>> getAllOrdems(Pageable pageable)
        throws URISyntaxException {
        Page<Ordem> page = ordemRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ordems");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ordems/:id -> get the "id" ordem.
     */
    @RequestMapping(value = "/ordems/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ordem> getOrdem(@PathVariable Long id) {
        log.debug("REST request to get Ordem : {}", id);
        return Optional.ofNullable(ordemRepository.findOneWithEagerRelationships(id))
            .map(ordem -> new ResponseEntity<>(
                ordem,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ordems/:id -> delete the "id" ordem.
     */
    @RequestMapping(value = "/ordems/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOrdem(@PathVariable Long id) {
        log.debug("REST request to delete Ordem : {}", id);
        ordemRepository.delete(id);
        ordemSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ordem", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ordems/:query -> search for the ordem corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/ordems/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ordem> searchOrdems(@PathVariable String query) {
        return StreamSupport
            .stream(ordemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
