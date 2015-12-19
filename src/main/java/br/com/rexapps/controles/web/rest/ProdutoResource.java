package br.com.rexapps.controles.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.validation.Valid;

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

import br.com.rexapps.controles.domain.Produto;
import br.com.rexapps.controles.repository.ProdutoRepository;
import br.com.rexapps.controles.repository.UserRepository;
import br.com.rexapps.controles.repository.search.ProdutoSearchRepository;
import br.com.rexapps.controles.security.SecurityUtils;
import br.com.rexapps.controles.service.EstoqueService;
import br.com.rexapps.controles.web.rest.util.HeaderUtil;
import br.com.rexapps.controles.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Produto.
 */
@RestController
@RequestMapping("/api")
public class ProdutoResource {

    private final Logger log = LoggerFactory.getLogger(ProdutoResource.class);

    @Inject
    private ProdutoRepository produtoRepository;

    @Inject
    private ProdutoSearchRepository produtoSearchRepository;
    
    @Inject
    private EstoqueService estoqueService; 
    
    @Inject
    private UserRepository userRepository;

    /**
     * POST  /produtos -> Create a new produto.
     */
    @RequestMapping(value = "/produtos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Produto> createProduto(@Valid @RequestBody Produto produto) throws URISyntaxException {
         log.debug("REST request to save Produto : {}", produto);
        if (produto.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new produto cannot already have an ID").body(null);
        }       
        
        produto.setDataCadastro(LocalDate.now());
        produto.setUser(userRepository.findOne(SecurityUtils.getCurrentUserId())); 
        Produto result = produtoRepository.save(produto);
        
        //dar entrada no estoque
        estoqueService.adicionarProdutoEstoque(produto);
        
        produtoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/produtos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("produto", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /produtos -> Updates an existing produto.
     */
    @RequestMapping(value = "/produtos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Produto> updateProduto(@Valid @RequestBody Produto produto) throws URISyntaxException {
        log.debug("REST request to update Produto : {}", produto);
        if (produto.getId() == null) {
            return createProduto(produto);
        }
        Produto result = produtoRepository.save(produto);
        produtoSearchRepository.save(produto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("produto", produto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /produtos -> get all the produtos.
     */
    @RequestMapping(value = "/produtos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Produto>> getAllProdutos(Pageable pageable)
        throws URISyntaxException {
        Page<Produto> page = produtoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/produtos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /produtos/:id -> get the "id" produto.
     */
    @RequestMapping(value = "/produtos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Produto> getProduto(@PathVariable Long id) {
        log.debug("REST request to get Produto : {}", id);
        return Optional.ofNullable(produtoRepository.findOne(id))
            .map(produto -> new ResponseEntity<>(
                produto,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /produtos/:id -> delete the "id" produto.
     */
    @RequestMapping(value = "/produtos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        log.debug("REST request to delete Produto : {}", id);
        produtoRepository.delete(id);
        produtoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("produto", id.toString())).build();
    }

    /**
     * SEARCH  /_search/produtos/:query -> search for the produto corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/produtos/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Produto> searchProdutos(@PathVariable String query) {
        return StreamSupport
            .stream(produtoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
}
