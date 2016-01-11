package br.com.rexapps.controles.web.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.com.rexapps.controles.domain.ClienteProduto;
import br.com.rexapps.controles.repository.ClienteProdutoRepository;
import br.com.rexapps.controles.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Cliente.
 */
@RestController
@RequestMapping("/api")
public class ClienteProdutoResource {

    private final Logger log = LoggerFactory.getLogger(ClienteProdutoResource.class);

    @Inject
    private ClienteProdutoRepository clienteProdutoRepository;


    /**
     * PUT  /cliente/produtos -> Updates an existing cliente.
     */
    @RequestMapping(value = "/clientes/produtos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ClienteProduto> updateClienteProdutos(@Valid @RequestBody ClienteProduto clienteProduto) throws URISyntaxException {
        log.debug("REST request to updateClienteProdutos: {}", clienteProduto);       
        clienteProduto.setPrecoVenda(clienteProduto.getProduto().getPrecoVenda());
        ClienteProduto result = clienteProdutoRepository.save(clienteProduto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cliente", clienteProduto.getId().toString()))
            .body(result);
    }
    
    /**
     * GET  /clientes/produtos/:id -> get the "id" cliente.
     */
    @RequestMapping(value = "/clientes/produtos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ClienteProduto>> getProdutosCliente(@PathVariable Long id) {
        log.debug("REST request to get Produtos de Cliente : {}", id);
        return Optional.ofNullable(clienteProdutoRepository.findAllbyCliente(id))
            .map(clienteProduto -> new ResponseEntity<>(
            	clienteProduto,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
	
	@RequestMapping(value = "/clientes/produtos/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> deleteClienteProduto(@PathVariable Long id) {
		log.debug("REST request to delete Pedido : {}", id);
		List<ClienteProduto> lista = clienteProdutoRepository.findAllbyCliente(id);
		for (ClienteProduto clienteProduto : lista) {
			clienteProdutoRepository.delete(clienteProduto.getId());	
		}
				
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cliente", id.toString())).build();
	}
    
}
