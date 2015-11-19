package br.com.rexapps.controles.web.rest;

import br.com.rexapps.controles.Application;
import br.com.rexapps.controles.domain.Ordem;
import br.com.rexapps.controles.repository.OrdemRepository;
import br.com.rexapps.controles.repository.search.OrdemSearchRepository;

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
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the OrdemResource REST controller.
 *
 * @see OrdemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OrdemResourceTest {


    private static final LocalDate DEFAULT_PERIODO_PEDIDO_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIODO_PEDIDO_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PERIODO_PEDIDO_FIM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIODO_PEDIDO_FIM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DT_PREVISTA_SEPARACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_PREVISTA_SEPARACAO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DT_REAL_SEPARACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_REAL_SEPARACAO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DT_PREVISTA_ENTREGA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_PREVISTA_ENTREGA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DT_REAL_ENTREGA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_REAL_ENTREGA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATA_PEDIDO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_PEDIDO = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private OrdemRepository ordemRepository;

    @Inject
    private OrdemSearchRepository ordemSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOrdemMockMvc;

    private Ordem ordem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrdemResource ordemResource = new OrdemResource();
        ReflectionTestUtils.setField(ordemResource, "ordemRepository", ordemRepository);
        ReflectionTestUtils.setField(ordemResource, "ordemSearchRepository", ordemSearchRepository);
        this.restOrdemMockMvc = MockMvcBuilders.standaloneSetup(ordemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ordem = new Ordem();
        ordem.setPeriodoPedidoInicio(DEFAULT_PERIODO_PEDIDO_INICIO);
        ordem.setPeriodoPedidoFim(DEFAULT_PERIODO_PEDIDO_FIM);
        ordem.setDtPrevistaSeparacao(DEFAULT_DT_PREVISTA_SEPARACAO);
        ordem.setDtRealSeparacao(DEFAULT_DT_REAL_SEPARACAO);
        ordem.setDtPrevistaEntrega(DEFAULT_DT_PREVISTA_ENTREGA);
        ordem.setDtRealEntrega(DEFAULT_DT_REAL_ENTREGA);
        ordem.setDataPedido(DEFAULT_DATA_PEDIDO);
    }

    @Test
    @Transactional
    public void createOrdem() throws Exception {
        int databaseSizeBeforeCreate = ordemRepository.findAll().size();

        // Create the Ordem

        restOrdemMockMvc.perform(post("/api/ordems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ordem)))
                .andExpect(status().isCreated());

        // Validate the Ordem in the database
        List<Ordem> ordems = ordemRepository.findAll();
        assertThat(ordems).hasSize(databaseSizeBeforeCreate + 1);
        Ordem testOrdem = ordems.get(ordems.size() - 1);
        assertThat(testOrdem.getPeriodoPedidoInicio()).isEqualTo(DEFAULT_PERIODO_PEDIDO_INICIO);
        assertThat(testOrdem.getPeriodoPedidoFim()).isEqualTo(DEFAULT_PERIODO_PEDIDO_FIM);
        assertThat(testOrdem.getDtPrevistaSeparacao()).isEqualTo(DEFAULT_DT_PREVISTA_SEPARACAO);
        assertThat(testOrdem.getDtRealSeparacao()).isEqualTo(DEFAULT_DT_REAL_SEPARACAO);
        assertThat(testOrdem.getDtPrevistaEntrega()).isEqualTo(DEFAULT_DT_PREVISTA_ENTREGA);
        assertThat(testOrdem.getDtRealEntrega()).isEqualTo(DEFAULT_DT_REAL_ENTREGA);
        assertThat(testOrdem.getDataPedido()).isEqualTo(DEFAULT_DATA_PEDIDO);
    }

    @Test
    @Transactional
    public void getAllOrdems() throws Exception {
        // Initialize the database
        ordemRepository.saveAndFlush(ordem);

        // Get all the ordems
        restOrdemMockMvc.perform(get("/api/ordems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ordem.getId().intValue())))
                .andExpect(jsonPath("$.[*].periodoPedidoInicio").value(hasItem(DEFAULT_PERIODO_PEDIDO_INICIO.toString())))
                .andExpect(jsonPath("$.[*].periodoPedidoFim").value(hasItem(DEFAULT_PERIODO_PEDIDO_FIM.toString())))
                .andExpect(jsonPath("$.[*].dtPrevistaSeparacao").value(hasItem(DEFAULT_DT_PREVISTA_SEPARACAO.toString())))
                .andExpect(jsonPath("$.[*].dtRealSeparacao").value(hasItem(DEFAULT_DT_REAL_SEPARACAO.toString())))
                .andExpect(jsonPath("$.[*].dtPrevistaEntrega").value(hasItem(DEFAULT_DT_PREVISTA_ENTREGA.toString())))
                .andExpect(jsonPath("$.[*].dtRealEntrega").value(hasItem(DEFAULT_DT_REAL_ENTREGA.toString())))
                .andExpect(jsonPath("$.[*].dataPedido").value(hasItem(DEFAULT_DATA_PEDIDO.toString())));
    }

    @Test
    @Transactional
    public void getOrdem() throws Exception {
        // Initialize the database
        ordemRepository.saveAndFlush(ordem);

        // Get the ordem
        restOrdemMockMvc.perform(get("/api/ordems/{id}", ordem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ordem.getId().intValue()))
            .andExpect(jsonPath("$.periodoPedidoInicio").value(DEFAULT_PERIODO_PEDIDO_INICIO.toString()))
            .andExpect(jsonPath("$.periodoPedidoFim").value(DEFAULT_PERIODO_PEDIDO_FIM.toString()))
            .andExpect(jsonPath("$.dtPrevistaSeparacao").value(DEFAULT_DT_PREVISTA_SEPARACAO.toString()))
            .andExpect(jsonPath("$.dtRealSeparacao").value(DEFAULT_DT_REAL_SEPARACAO.toString()))
            .andExpect(jsonPath("$.dtPrevistaEntrega").value(DEFAULT_DT_PREVISTA_ENTREGA.toString()))
            .andExpect(jsonPath("$.dtRealEntrega").value(DEFAULT_DT_REAL_ENTREGA.toString()))
            .andExpect(jsonPath("$.dataPedido").value(DEFAULT_DATA_PEDIDO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOrdem() throws Exception {
        // Get the ordem
        restOrdemMockMvc.perform(get("/api/ordems/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrdem() throws Exception {
        // Initialize the database
        ordemRepository.saveAndFlush(ordem);

		int databaseSizeBeforeUpdate = ordemRepository.findAll().size();

        // Update the ordem
        ordem.setPeriodoPedidoInicio(UPDATED_PERIODO_PEDIDO_INICIO);
        ordem.setPeriodoPedidoFim(UPDATED_PERIODO_PEDIDO_FIM);
        ordem.setDtPrevistaSeparacao(UPDATED_DT_PREVISTA_SEPARACAO);
        ordem.setDtRealSeparacao(UPDATED_DT_REAL_SEPARACAO);
        ordem.setDtPrevistaEntrega(UPDATED_DT_PREVISTA_ENTREGA);
        ordem.setDtRealEntrega(UPDATED_DT_REAL_ENTREGA);
        ordem.setDataPedido(UPDATED_DATA_PEDIDO);

        restOrdemMockMvc.perform(put("/api/ordems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ordem)))
                .andExpect(status().isOk());

        // Validate the Ordem in the database
        List<Ordem> ordems = ordemRepository.findAll();
        assertThat(ordems).hasSize(databaseSizeBeforeUpdate);
        Ordem testOrdem = ordems.get(ordems.size() - 1);
        assertThat(testOrdem.getPeriodoPedidoInicio()).isEqualTo(UPDATED_PERIODO_PEDIDO_INICIO);
        assertThat(testOrdem.getPeriodoPedidoFim()).isEqualTo(UPDATED_PERIODO_PEDIDO_FIM);
        assertThat(testOrdem.getDtPrevistaSeparacao()).isEqualTo(UPDATED_DT_PREVISTA_SEPARACAO);
        assertThat(testOrdem.getDtRealSeparacao()).isEqualTo(UPDATED_DT_REAL_SEPARACAO);
        assertThat(testOrdem.getDtPrevistaEntrega()).isEqualTo(UPDATED_DT_PREVISTA_ENTREGA);
        assertThat(testOrdem.getDtRealEntrega()).isEqualTo(UPDATED_DT_REAL_ENTREGA);
        assertThat(testOrdem.getDataPedido()).isEqualTo(UPDATED_DATA_PEDIDO);
    }

    @Test
    @Transactional
    public void deleteOrdem() throws Exception {
        // Initialize the database
        ordemRepository.saveAndFlush(ordem);

		int databaseSizeBeforeDelete = ordemRepository.findAll().size();

        // Get the ordem
        restOrdemMockMvc.perform(delete("/api/ordems/{id}", ordem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Ordem> ordems = ordemRepository.findAll();
        assertThat(ordems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
