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

import br.com.rexapps.controles.domain.Estoque;
import br.com.rexapps.controles.domain.enumeration.OperacaoEstoque;
import br.com.rexapps.controles.repository.EstoqueRepository;
import br.com.rexapps.controles.repository.ProdutoRepository;
import br.com.rexapps.controles.repository.UserRepository;
import br.com.rexapps.controles.repository.search.EstoqueSearchRepository;
import br.com.rexapps.controles.security.SecurityUtils;
import br.com.rexapps.controles.web.rest.util.HeaderUtil;
import br.com.rexapps.controles.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Estoque.
 */
@RestController
@RequestMapping("/api")
public class EstoqueResource {

    private final Logger log = LoggerFactory.getLogger(EstoqueResource.class);

    @Inject
    private EstoqueRepository estoqueRepository;

    @Inject
    private ProdutoRepository produtoRepository;

    
    @Inject
    private EstoqueSearchRepository estoqueSearchRepository;
    
    @Inject
	private UserRepository userRepository;

    /**
     * POST  /estoques -> Create a new estoque.
     */
    @RequestMapping(value = "/estoques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estoque> createEstoque(@RequestBody Estoque estoque) throws URISyntaxException {
        log.debug("REST request to save Estoque : {}", estoque);
        if (estoque.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new estoque cannot already have an ID").body(null);
        }
        estoque.setDataAtual(LocalDate.now()); 
        estoque.setEstoque_user(userRepository.findOne(SecurityUtils.getCurrentUserId()));
        if(estoque.getOperacao() == OperacaoEstoque.Entrada){
        	estoque.setQuantidadeAtual(estoque.getEstoque_produto().getEstoque());
        	estoque.setQuantidadeAposMovimentacao(estoque.getEstoque_produto().getEstoque() + estoque.getQuantidade());
        	estoque.getEstoque_produto().setEstoque(estoque.getQuantidadeAposMovimentacao());
        }else{
        	estoque.setQuantidadeAtual(estoque.getEstoque_produto().getEstoque());
        	estoque.setQuantidadeAposMovimentacao(estoque.getEstoque_produto().getEstoque() - estoque.getQuantidade());
        	estoque.getEstoque_produto().setEstoque(estoque.getQuantidadeAposMovimentacao());
        }
        produtoRepository.save(estoque.getEstoque_produto());
        Estoque result = estoqueRepository.save(estoque);
        estoqueSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/estoques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("estoque", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /estoques -> Updates an existing estoque.
     */
    @RequestMapping(value = "/estoques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estoque> updateEstoque(@RequestBody Estoque estoque) throws URISyntaxException {
        log.debug("REST request to update Estoque : {}", estoque);
        if (estoque.getId() == null) {
            return createEstoque(estoque);
        }
        Estoque result = estoqueRepository.save(estoque);
        estoqueSearchRepository.save(estoque);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("estoque", estoque.getId().toString()))
            .body(result);
    }

    /**
     * GET  /estoques -> get all the estoques.
     */
    @RequestMapping(value = "/estoques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Estoque>> getAllEstoques(Pageable pageable)
        throws URISyntaxException {
        Page<Estoque> page = estoqueRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/estoques");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /estoques/:id -> get the "id" estoque.
     */
    @RequestMapping(value = "/estoques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Estoque> getEstoque(@PathVariable Long id) {
        log.debug("REST request to get Estoque : {}", id);
        return Optional.ofNullable(estoqueRepository.findOne(id))
            .map(estoque -> new ResponseEntity<>(
                estoque,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /estoques/:id -> delete the "id" estoque.
     */
    @RequestMapping(value = "/estoques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEstoque(@PathVariable Long id) {
        log.debug("REST request to delete Estoque : {}", id);
        estoqueRepository.delete(id);
        estoqueSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("estoque", id.toString())).build();
    }

    /**
     * SEARCH  /_search/estoques/:query -> search for the estoque corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/estoques/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Estoque> searchEstoques(@PathVariable String query) {
        return StreamSupport
            .stream(estoqueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
