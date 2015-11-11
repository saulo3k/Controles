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

import br.com.rexapps.controles.domain.Email;
import br.com.rexapps.controles.repository.EmailRepository;
import br.com.rexapps.controles.repository.search.EmailSearchRepository;
import br.com.rexapps.controles.service.MailService;
import br.com.rexapps.controles.web.rest.util.HeaderUtil;
import br.com.rexapps.controles.web.rest.util.PaginationUtil;

/**
 * REST controller for managing Email.
 */
@RestController
@RequestMapping("/api")
public class EmailResource {

    private final Logger log = LoggerFactory.getLogger(EmailResource.class);

    @Inject
    private EmailRepository emailRepository;
    
    @Inject
    MailService emailServices;

    @Inject
    private EmailSearchRepository emailSearchRepository;

    /**
     * POST  /emails -> Create a new email.
     */
    @RequestMapping(value = "/emails",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Email> createEmail(@RequestBody Email email) throws URISyntaxException {
        log.debug("REST request to save Email : {}", email);
        if (email.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new email cannot already have an ID").body(null);
        }
        emailServices.sendEmail("rexappsbr@gmail.com", "teste", "tr", false, false);
        Email result = emailRepository.save(email);
        emailSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/emails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("email", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /emails -> Updates an existing email.
     */
    @RequestMapping(value = "/emails",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Email> updateEmail(@RequestBody Email email) throws URISyntaxException {
        log.debug("REST request to update Email : {}", email);
        if (email.getId() == null) {
            return createEmail(email);
        }
        Email result = emailRepository.save(email);
        emailSearchRepository.save(email);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("email", email.getId().toString()))
            .body(result);
    }

    /**
     * GET  /emails -> get all the emails.
     */
    @RequestMapping(value = "/emails",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Email>> getAllEmails(Pageable pageable)
        throws URISyntaxException {
        Page<Email> page = emailRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/emails");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /emails/:id -> get the "id" email.
     */
    @RequestMapping(value = "/emails/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Email> getEmail(@PathVariable Long id) {
        log.debug("REST request to get Email : {}", id);
        return Optional.ofNullable(emailRepository.findOne(id))
            .map(email -> new ResponseEntity<>(
                email,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /emails/:id -> delete the "id" email.
     */
    @RequestMapping(value = "/emails/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        log.debug("REST request to delete Email : {}", id);
        emailRepository.delete(id);
        emailSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("email", id.toString())).build();
    }

    /**
     * SEARCH  /_search/emails/:query -> search for the email corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/emails/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Email> searchEmails(@PathVariable String query) {
        return StreamSupport
            .stream(emailSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
