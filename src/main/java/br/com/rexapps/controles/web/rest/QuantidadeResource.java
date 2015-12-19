package br.com.rexapps.controles.web.rest;

import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.com.rexapps.controles.repository.CategoriaProdutoRepository;
import br.com.rexapps.controles.repository.ClienteRepository;
import br.com.rexapps.controles.repository.PedidoRepository;
import br.com.rexapps.controles.repository.ProdutoRepository;
import br.com.rexapps.controles.repository.UserRepository;
import br.com.rexapps.controles.web.rest.dto.QuantidadeDTO;

/**
 * REST controller for managing Cliente.
 */
@RestController
@RequestMapping("/api")
public class QuantidadeResource {

    private final Logger log = LoggerFactory.getLogger(QuantidadeResource.class);

    @Inject
    private ClienteRepository clienteRepository;

    @Inject
    private ProdutoRepository produtoRepository;
    
    @Inject
    private PedidoRepository pedidoRepository;
    
    @Inject
    private CategoriaProdutoRepository categoriaProdutoRepository;
    
    @Inject
    private UserRepository userRepository;
    
    
    /**
     * GET  /clientes/:id -> get the "id" cliente.
     */
    @RequestMapping(value = "/quantidade",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<QuantidadeDTO> getCount() {
    	QuantidadeDTO qnti = new QuantidadeDTO();    	
    	qnti.setQuantidadeCliente(clienteRepository.count());
    	qnti.setQuantidadeEstoque(produtoRepository.countProdutos());
    	qnti.setQuantidadeProduto(produtoRepository.count());
        qnti.setQuantidadeCategoria(categoriaProdutoRepository.count());
        qnti.setQuantidadePedido(pedidoRepository.count());
        qnti.setQuantidadeSeparacao(pedidoRepository.countPedidosSeparacao());
        qnti.setQuantidadeEntrega(pedidoRepository.countPedidosEntrega());
        qnti.setQuantidadeUsuario(userRepository.count());
        return Optional.ofNullable(qnti)
            .map(cliente -> new ResponseEntity<>(
                cliente,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
