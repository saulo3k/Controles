package br.com.rexapps.controles.web.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import br.com.rexapps.controles.domain.DiaSemana;
import br.com.rexapps.controles.repository.DiasSemanaRepository;

/**
 * REST controller for managing Pedido.
 */
@RestController
@RequestMapping("/api")
public class DiasSemanaResource {

    private final Logger log = LoggerFactory.getLogger(DiasSemanaResource.class);

    @Inject
    private DiasSemanaRepository diasSemanaRepository;
    
 


    /**
     * GET  /diasSemana -> get all the pedidos.
     */
    @RequestMapping(value = "/diasSemana",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DiaSemana>> getAllDiasSemana()
        throws URISyntaxException {
        return Optional.ofNullable(diasSemanaRepository.findAll())
                .map(diaSemana -> new ResponseEntity<>(diaSemana,HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /diasSemana/:id -> get the "id" pedido.
     */
    @RequestMapping(value = "/diasSemana/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DiaSemana> getPedido(@PathVariable Long id) {
        log.debug("REST request to get Pedido : {}", id);
        return Optional.ofNullable(diasSemanaRepository.findOne(id))
            .map(diaSemana -> new ResponseEntity<>(diaSemana,HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

  
}
