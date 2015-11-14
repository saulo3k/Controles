package br.com.rexapps.controles.web.rest;

import br.com.rexapps.controles.Application;
import br.com.rexapps.controles.domain.Pedido;
import br.com.rexapps.controles.repository.PedidoRepository;
import br.com.rexapps.controles.repository.search.PedidoSearchRepository;

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

import br.com.rexapps.controles.domain.enumeration.DiaSemana;

/**
 * Test class for the PedidoResource REST controller.
 *
 * @see PedidoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PedidoResourceTest {


    private static final LocalDate DEFAULT_DT_PREVISTA_SEPARACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_PREVISTA_SEPARACAO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DT_REAL_SEPARACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_REAL_SEPARACAO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DT_PREVISTA_ENTREGA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_PREVISTA_ENTREGA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DT_REAL_ENTREGA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DT_REAL_ENTREGA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PERIODO_PEDIDO_INICIO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIODO_PEDIDO_INICIO = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PERIODO_PEDIDO_FIM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIODO_PEDIDO_FIM = LocalDate.now(ZoneId.systemDefault());


private static final DiaSemana DEFAULT_DIA_SEMANA = DiaSemana.Segunda;
    private static final DiaSemana UPDATED_DIA_SEMANA = DiaSemana.Terca;

    private static final LocalDate DEFAULT_DATA_PEDIDO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_PEDIDO = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_USUARIO_QUE_FEZ_PEDIDO = 1;
    private static final Integer UPDATED_USUARIO_QUE_FEZ_PEDIDO = 2;

    @Inject
    private PedidoRepository pedidoRepository;

    @Inject
    private PedidoSearchRepository pedidoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPedidoMockMvc;

    private Pedido pedido;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PedidoResource pedidoResource = new PedidoResource();
        ReflectionTestUtils.setField(pedidoResource, "pedidoRepository", pedidoRepository);
        ReflectionTestUtils.setField(pedidoResource, "pedidoSearchRepository", pedidoSearchRepository);
        this.restPedidoMockMvc = MockMvcBuilders.standaloneSetup(pedidoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        pedido = new Pedido();
        pedido.setDtPrevistaSeparacao(DEFAULT_DT_PREVISTA_SEPARACAO);
        pedido.setDtRealSeparacao(DEFAULT_DT_REAL_SEPARACAO);
        pedido.setDtPrevistaEntrega(DEFAULT_DT_PREVISTA_ENTREGA);
        pedido.setDtRealEntrega(DEFAULT_DT_REAL_ENTREGA);
        pedido.setPeriodoPedidoInicio(DEFAULT_PERIODO_PEDIDO_INICIO);
        pedido.setPeriodoPedidoFim(DEFAULT_PERIODO_PEDIDO_FIM);
        pedido.setDiaSemana(DEFAULT_DIA_SEMANA);
        pedido.setDataPedido(DEFAULT_DATA_PEDIDO);
        pedido.setUsuarioQueFezPedido(DEFAULT_USUARIO_QUE_FEZ_PEDIDO);
    }

    @Test
    @Transactional
    public void createPedido() throws Exception {
        int databaseSizeBeforeCreate = pedidoRepository.findAll().size();

        // Create the Pedido

        restPedidoMockMvc.perform(post("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedido)))
                .andExpect(status().isCreated());

        // Validate the Pedido in the database
        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeCreate + 1);
        Pedido testPedido = pedidos.get(pedidos.size() - 1);
        assertThat(testPedido.getDtPrevistaSeparacao()).isEqualTo(DEFAULT_DT_PREVISTA_SEPARACAO);
        assertThat(testPedido.getDtRealSeparacao()).isEqualTo(DEFAULT_DT_REAL_SEPARACAO);
        assertThat(testPedido.getDtPrevistaEntrega()).isEqualTo(DEFAULT_DT_PREVISTA_ENTREGA);
        assertThat(testPedido.getDtRealEntrega()).isEqualTo(DEFAULT_DT_REAL_ENTREGA);
        assertThat(testPedido.getPeriodoPedidoInicio()).isEqualTo(DEFAULT_PERIODO_PEDIDO_INICIO);
        assertThat(testPedido.getPeriodoPedidoFim()).isEqualTo(DEFAULT_PERIODO_PEDIDO_FIM);
        assertThat(testPedido.getDiaSemana()).isEqualTo(DEFAULT_DIA_SEMANA);
        assertThat(testPedido.getDataPedido()).isEqualTo(DEFAULT_DATA_PEDIDO);
        assertThat(testPedido.getUsuarioQueFezPedido()).isEqualTo(DEFAULT_USUARIO_QUE_FEZ_PEDIDO);
    }

    @Test
    @Transactional
    public void checkDtRealSeparacaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pedidoRepository.findAll().size();
        // set the field null
        pedido.setDtRealSeparacao(null);

        // Create the Pedido, which fails.

        restPedidoMockMvc.perform(post("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedido)))
                .andExpect(status().isBadRequest());

        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPedidos() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidos
        restPedidoMockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pedido.getId().intValue())))
                .andExpect(jsonPath("$.[*].dtPrevistaSeparacao").value(hasItem(DEFAULT_DT_PREVISTA_SEPARACAO.toString())))
                .andExpect(jsonPath("$.[*].dtRealSeparacao").value(hasItem(DEFAULT_DT_REAL_SEPARACAO.toString())))
                .andExpect(jsonPath("$.[*].dtPrevistaEntrega").value(hasItem(DEFAULT_DT_PREVISTA_ENTREGA.toString())))
                .andExpect(jsonPath("$.[*].dtRealEntrega").value(hasItem(DEFAULT_DT_REAL_ENTREGA.toString())))
                .andExpect(jsonPath("$.[*].periodoPedidoInicio").value(hasItem(DEFAULT_PERIODO_PEDIDO_INICIO.toString())))
                .andExpect(jsonPath("$.[*].periodoPedidoFim").value(hasItem(DEFAULT_PERIODO_PEDIDO_FIM.toString())))
                .andExpect(jsonPath("$.[*].diaSemana").value(hasItem(DEFAULT_DIA_SEMANA.toString())))
                .andExpect(jsonPath("$.[*].dataPedido").value(hasItem(DEFAULT_DATA_PEDIDO.toString())))
                .andExpect(jsonPath("$.[*].usuarioQueFezPedido").value(hasItem(DEFAULT_USUARIO_QUE_FEZ_PEDIDO)));
    }

    @Test
    @Transactional
    public void getPedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get the pedido
        restPedidoMockMvc.perform(get("/api/pedidos/{id}", pedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(pedido.getId().intValue()))
            .andExpect(jsonPath("$.dtPrevistaSeparacao").value(DEFAULT_DT_PREVISTA_SEPARACAO.toString()))
            .andExpect(jsonPath("$.dtRealSeparacao").value(DEFAULT_DT_REAL_SEPARACAO.toString()))
            .andExpect(jsonPath("$.dtPrevistaEntrega").value(DEFAULT_DT_PREVISTA_ENTREGA.toString()))
            .andExpect(jsonPath("$.dtRealEntrega").value(DEFAULT_DT_REAL_ENTREGA.toString()))
            .andExpect(jsonPath("$.periodoPedidoInicio").value(DEFAULT_PERIODO_PEDIDO_INICIO.toString()))
            .andExpect(jsonPath("$.periodoPedidoFim").value(DEFAULT_PERIODO_PEDIDO_FIM.toString()))
            .andExpect(jsonPath("$.diaSemana").value(DEFAULT_DIA_SEMANA.toString()))
            .andExpect(jsonPath("$.dataPedido").value(DEFAULT_DATA_PEDIDO.toString()))
            .andExpect(jsonPath("$.usuarioQueFezPedido").value(DEFAULT_USUARIO_QUE_FEZ_PEDIDO));
    }

    @Test
    @Transactional
    public void getNonExistingPedido() throws Exception {
        // Get the pedido
        restPedidoMockMvc.perform(get("/api/pedidos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

		int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();

        // Update the pedido
        pedido.setDtPrevistaSeparacao(UPDATED_DT_PREVISTA_SEPARACAO);
        pedido.setDtRealSeparacao(UPDATED_DT_REAL_SEPARACAO);
        pedido.setDtPrevistaEntrega(UPDATED_DT_PREVISTA_ENTREGA);
        pedido.setDtRealEntrega(UPDATED_DT_REAL_ENTREGA);
        pedido.setPeriodoPedidoInicio(UPDATED_PERIODO_PEDIDO_INICIO);
        pedido.setPeriodoPedidoFim(UPDATED_PERIODO_PEDIDO_FIM);
        pedido.setDiaSemana(UPDATED_DIA_SEMANA);
        pedido.setDataPedido(UPDATED_DATA_PEDIDO);
        pedido.setUsuarioQueFezPedido(UPDATED_USUARIO_QUE_FEZ_PEDIDO);

        restPedidoMockMvc.perform(put("/api/pedidos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pedido)))
                .andExpect(status().isOk());

        // Validate the Pedido in the database
        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeUpdate);
        Pedido testPedido = pedidos.get(pedidos.size() - 1);
        assertThat(testPedido.getDtPrevistaSeparacao()).isEqualTo(UPDATED_DT_PREVISTA_SEPARACAO);
        assertThat(testPedido.getDtRealSeparacao()).isEqualTo(UPDATED_DT_REAL_SEPARACAO);
        assertThat(testPedido.getDtPrevistaEntrega()).isEqualTo(UPDATED_DT_PREVISTA_ENTREGA);
        assertThat(testPedido.getDtRealEntrega()).isEqualTo(UPDATED_DT_REAL_ENTREGA);
        assertThat(testPedido.getPeriodoPedidoInicio()).isEqualTo(UPDATED_PERIODO_PEDIDO_INICIO);
        assertThat(testPedido.getPeriodoPedidoFim()).isEqualTo(UPDATED_PERIODO_PEDIDO_FIM);
        assertThat(testPedido.getDiaSemana()).isEqualTo(UPDATED_DIA_SEMANA);
        assertThat(testPedido.getDataPedido()).isEqualTo(UPDATED_DATA_PEDIDO);
        assertThat(testPedido.getUsuarioQueFezPedido()).isEqualTo(UPDATED_USUARIO_QUE_FEZ_PEDIDO);
    }

    @Test
    @Transactional
    public void deletePedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

		int databaseSizeBeforeDelete = pedidoRepository.findAll().size();

        // Get the pedido
        restPedidoMockMvc.perform(delete("/api/pedidos/{id}", pedido.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Pedido> pedidos = pedidoRepository.findAll();
        assertThat(pedidos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
