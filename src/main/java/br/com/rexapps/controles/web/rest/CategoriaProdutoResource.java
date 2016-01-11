package br.com.rexapps.controles.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.rexapps.controles.domain.CategoriaProduto;
import br.com.rexapps.controles.repository.CategoriaProdutoRepository;
import br.com.rexapps.controles.repository.search.CategoriaProdutoSearchRepository;
import br.com.rexapps.controles.web.rest.util.HeaderUtil;
import br.com.rexapps.controles.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
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
 * REST controller for managing CategoriaProduto.
 */
@RestController
@RequestMapping("/api")
public class CategoriaProdutoResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaProdutoResource.class);

    @Inject
    private CategoriaProdutoRepository categoriaProdutoRepository;

    @Inject
    private CategoriaProdutoSearchRepository categoriaProdutoSearchRepository;

    /**
     * POST  /categoriaProdutos -> Create a new categoriaProduto.
     */
    @RequestMapping(value = "/categoriaProdutos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CategoriaProduto> createCategoriaProduto(@Valid @RequestBody CategoriaProduto categoriaProduto) throws URISyntaxException {
        log.debug("REST request to save CategoriaProduto : {}", categoriaProduto);
        if (categoriaProduto.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new categoriaProduto cannot already have an ID").body(null);
        }
        CategoriaProduto result = categoriaProdutoRepository.save(categoriaProduto);
        categoriaProdutoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/categoriaProdutos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("categoriaProduto", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categoriaProdutos -> Updates an existing categoriaProduto.
     */
    @RequestMapping(value = "/categoriaProdutos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CategoriaProduto> updateCategoriaProduto(@Valid @RequestBody CategoriaProduto categoriaProduto) throws URISyntaxException {
        log.debug("REST request to update CategoriaProduto : {}", categoriaProduto);
        if (categoriaProduto.getId() == null) {
            return createCategoriaProduto(categoriaProduto);
        }
        CategoriaProduto result = categoriaProdutoRepository.save(categoriaProduto);
        categoriaProdutoSearchRepository.save(categoriaProduto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("categoriaProduto", categoriaProduto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categoriaProdutos -> get all the categoriaProdutos.
     */
    @RequestMapping(value = "/categoriaProdutos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CategoriaProduto>> getAllCategoriaProdutos(Pageable pageable)
        throws URISyntaxException {
        Page<CategoriaProduto> page = categoriaProdutoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/categoriaProdutos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /categoriaProdutos/:id -> get the "id" categoriaProduto.
     */
    @RequestMapping(value = "/categoriaProdutos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CategoriaProduto> getCategoriaProduto(@PathVariable Long id) {
        log.debug("REST request to get CategoriaProduto : {}", id);
        return Optional.ofNullable(categoriaProdutoRepository.findOne(id))
            .map(categoriaProduto -> new ResponseEntity<>(
                categoriaProduto,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /categoriaProdutos/:id -> delete the "id" categoriaProduto.
     */
    @RequestMapping(value = "/categoriaProdutos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCategoriaProduto(@PathVariable Long id) {
        log.debug("REST request to delete CategoriaProduto : {}", id);
        try{
        	categoriaProdutoRepository.delete(id);
            categoriaProdutoSearchRepository.delete(id);        	
        }catch(DataIntegrityViolationException p){        
        	return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("categoriaProduto.naoremover", id.toString())).build();
        }
               
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("categoriaProduto", id.toString())).build();
    }

    /**
     * SEARCH  /_search/categoriaProdutos/:query -> search for the categoriaProduto corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/categoriaProdutos/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CategoriaProduto> searchCategoriaProdutos(@PathVariable String query) {
        return StreamSupport
            .stream(categoriaProdutoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
