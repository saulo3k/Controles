package br.com.rexapps.controles.web.rest;

import br.com.rexapps.controles.Application;
import br.com.rexapps.controles.domain.PedTeste;
import br.com.rexapps.controles.repository.PedTesteRepository;
import br.com.rexapps.controles.repository.search.PedTesteSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PedTesteResource REST controller.
 *
 * @see PedTesteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PedTesteResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final LocalDate DEFAULT_DT_PREVISTA_ENTREGA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_PREVISTA_ENTREGA = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_DT_PREVISTA_ENTREGA_ZONED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DT_PREVISTA_ENTREGA_ZONED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DT_PREVISTA_ENTREGA_ZONED_STR = dateTimeFormatter.format(DEFAULT_DT_PREVISTA_ENTREGA_ZONED);

    @Inject
    private PedTesteRepository pedTesteRepository;

    @Inject
    private PedTesteSearchRepository pedTesteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPedTesteMockMvc;

    private PedTeste pedTeste;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PedTesteResource pedTesteResource = new PedTesteResource();
        ReflectionTestUtils.setField(pedTesteResource, "pedTesteRepository", pedTesteRepository);
        ReflectionTestUtils.setField(pedTesteResource, "pedTesteSearchRepository", pedTesteSearchRepository);
        this.restPedTesteMockMvc = MockMvcBuilders.standaloneSetup(pedTesteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pedTeste = new PedTeste();
        pedTeste.setDtPrevistaEntrega(DEFAULT_DT_PREVISTA_ENTREGA);
        pedTeste.setDtPrevistaEntregaZoned(DEFAULT_DT_PREVISTA_ENTREGA_ZONED);
    }

    @Test
    @Transactional
    public void createPedTeste() throws Exception {
        int databaseSizeBeforeCreate = pedTesteRepository.findAll().size();

        // Create the PedTeste

        restPedTesteMockMvc.perform(post("/api/pedTestes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedTeste)))
                .andExpect(status().isCreated());

        // Validate the PedTeste in the database
        List<PedTeste> pedTestes = pedTesteRepository.findAll();
        assertThat(pedTestes).hasSize(databaseSizeBeforeCreate + 1);
        PedTeste testPedTeste = pedTestes.get(pedTestes.size() - 1);
        assertThat(testPedTeste.getDtPrevistaEntrega()).isEqualTo(DEFAULT_DT_PREVISTA_ENTREGA);
        assertThat(testPedTeste.getDtPrevistaEntregaZoned()).isEqualTo(DEFAULT_DT_PREVISTA_ENTREGA_ZONED);
    }

    @Test
    @Transactional
    public void getAllPedTestes() throws Exception {
        // Initialize the database
        pedTesteRepository.saveAndFlush(pedTeste);

        // Get all the pedTestes
        restPedTesteMockMvc.perform(get("/api/pedTestes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pedTeste.getId().intValue())))
                .andExpect(jsonPath("$.[*].dtPrevistaEntrega").value(hasItem(DEFAULT_DT_PREVISTA_ENTREGA.toString())))
                .andExpect(jsonPath("$.[*].dtPrevistaEntregaZoned").value(hasItem(DEFAULT_DT_PREVISTA_ENTREGA_ZONED_STR)));
    }

    @Test
    @Transactional
    public void getPedTeste() throws Exception {
        // Initialize the database
        pedTesteRepository.saveAndFlush(pedTeste);

        // Get the pedTeste
        restPedTesteMockMvc.perform(get("/api/pedTestes/{id}", pedTeste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pedTeste.getId().intValue()))
            .andExpect(jsonPath("$.dtPrevistaEntrega").value(DEFAULT_DT_PREVISTA_ENTREGA.toString()))
            .andExpect(jsonPath("$.dtPrevistaEntregaZoned").value(DEFAULT_DT_PREVISTA_ENTREGA_ZONED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPedTeste() throws Exception {
        // Get the pedTeste
        restPedTesteMockMvc.perform(get("/api/pedTestes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePedTeste() throws Exception {
        // Initialize the database
        pedTesteRepository.saveAndFlush(pedTeste);

		int databaseSizeBeforeUpdate = pedTesteRepository.findAll().size();

        // Update the pedTeste
        pedTeste.setDtPrevistaEntrega(UPDATED_DT_PREVISTA_ENTREGA);
        pedTeste.setDtPrevistaEntregaZoned(UPDATED_DT_PREVISTA_ENTREGA_ZONED);

        restPedTesteMockMvc.perform(put("/api/pedTestes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedTeste)))
                .andExpect(status().isOk());

        // Validate the PedTeste in the database
        List<PedTeste> pedTestes = pedTesteRepository.findAll();
        assertThat(pedTestes).hasSize(databaseSizeBeforeUpdate);
        PedTeste testPedTeste = pedTestes.get(pedTestes.size() - 1);
        assertThat(testPedTeste.getDtPrevistaEntrega()).isEqualTo(UPDATED_DT_PREVISTA_ENTREGA);
        assertThat(testPedTeste.getDtPrevistaEntregaZoned()).isEqualTo(UPDATED_DT_PREVISTA_ENTREGA_ZONED);
    }

    @Test
    @Transactional
    public void deletePedTeste() throws Exception {
        // Initialize the database
        pedTesteRepository.saveAndFlush(pedTeste);

		int databaseSizeBeforeDelete = pedTesteRepository.findAll().size();

        // Get the pedTeste
        restPedTesteMockMvc.perform(delete("/api/pedTestes/{id}", pedTeste.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PedTeste> pedTestes = pedTesteRepository.findAll();
        assertThat(pedTestes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
