package br.com.rexapps.controles.web.rest;

import br.com.rexapps.controles.Application;
import br.com.rexapps.controles.domain.Estoque;
import br.com.rexapps.controles.repository.EstoqueRepository;
import br.com.rexapps.controles.repository.search.EstoqueSearchRepository;

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

import br.com.rexapps.controles.domain.enumeration.OperacaoEstoque;

/**
 * Test class for the EstoqueResource REST controller.
 *
 * @see EstoqueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EstoqueResourceTest {


    private static final Long DEFAULT_QUANTIDADE_ATUAL = 1L;
    private static final Long UPDATED_QUANTIDADE_ATUAL = 2L;

    private static final Long DEFAULT_QUANTIDADE_APOS_MOVIMENTACAO = 1L;
    private static final Long UPDATED_QUANTIDADE_APOS_MOVIMENTACAO = 2L;

    private static final LocalDate DEFAULT_DATA_ATUAL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_ATUAL = LocalDate.now(ZoneId.systemDefault());


private static final OperacaoEstoque DEFAULT_OPERACAO = OperacaoEstoque.Entrada;
    private static final OperacaoEstoque UPDATED_OPERACAO = OperacaoEstoque.Saida;
    private static final String DEFAULT_MOTIVO = "AAAAA";
    private static final String UPDATED_MOTIVO = "BBBBB";

    @Inject
    private EstoqueRepository estoqueRepository;

    @Inject
    private EstoqueSearchRepository estoqueSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEstoqueMockMvc;

    private Estoque estoque;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EstoqueResource estoqueResource = new EstoqueResource();
        ReflectionTestUtils.setField(estoqueResource, "estoqueRepository", estoqueRepository);
        ReflectionTestUtils.setField(estoqueResource, "estoqueSearchRepository", estoqueSearchRepository);
        this.restEstoqueMockMvc = MockMvcBuilders.standaloneSetup(estoqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        estoque = new Estoque();
        estoque.setQuantidadeAtual(DEFAULT_QUANTIDADE_ATUAL);
        estoque.setQuantidadeAposMovimentacao(DEFAULT_QUANTIDADE_APOS_MOVIMENTACAO);
        estoque.setDataAtual(DEFAULT_DATA_ATUAL);
        estoque.setOperacao(DEFAULT_OPERACAO);
        estoque.setMotivo(DEFAULT_MOTIVO);
    }

    @Test
    @Transactional
    public void createEstoque() throws Exception {
        int databaseSizeBeforeCreate = estoqueRepository.findAll().size();

        // Create the Estoque

        restEstoqueMockMvc.perform(post("/api/estoques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estoque)))
                .andExpect(status().isCreated());

        // Validate the Estoque in the database
        List<Estoque> estoques = estoqueRepository.findAll();
        assertThat(estoques).hasSize(databaseSizeBeforeCreate + 1);
        Estoque testEstoque = estoques.get(estoques.size() - 1);
        assertThat(testEstoque.getQuantidadeAtual()).isEqualTo(DEFAULT_QUANTIDADE_ATUAL);
        assertThat(testEstoque.getQuantidadeAposMovimentacao()).isEqualTo(DEFAULT_QUANTIDADE_APOS_MOVIMENTACAO);
        assertThat(testEstoque.getDataAtual()).isEqualTo(DEFAULT_DATA_ATUAL);
        assertThat(testEstoque.getOperacao()).isEqualTo(DEFAULT_OPERACAO);
        assertThat(testEstoque.getMotivo()).isEqualTo(DEFAULT_MOTIVO);
    }

    @Test
    @Transactional
    public void getAllEstoques() throws Exception {
        // Initialize the database
        estoqueRepository.saveAndFlush(estoque);

        // Get all the estoques
        restEstoqueMockMvc.perform(get("/api/estoques"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(estoque.getId().intValue())))
                .andExpect(jsonPath("$.[*].quantidadeAtual").value(hasItem(DEFAULT_QUANTIDADE_ATUAL.intValue())))
                .andExpect(jsonPath("$.[*].quantidadeAposMovimentacao").value(hasItem(DEFAULT_QUANTIDADE_APOS_MOVIMENTACAO.intValue())))
                .andExpect(jsonPath("$.[*].dataAtual").value(hasItem(DEFAULT_DATA_ATUAL.toString())))
                .andExpect(jsonPath("$.[*].operacao").value(hasItem(DEFAULT_OPERACAO.toString())))
                .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO.toString())));
    }

    @Test
    @Transactional
    public void getEstoque() throws Exception {
        // Initialize the database
        estoqueRepository.saveAndFlush(estoque);

        // Get the estoque
        restEstoqueMockMvc.perform(get("/api/estoques/{id}", estoque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(estoque.getId().intValue()))
            .andExpect(jsonPath("$.quantidadeAtual").value(DEFAULT_QUANTIDADE_ATUAL.intValue()))
            .andExpect(jsonPath("$.quantidadeAposMovimentacao").value(DEFAULT_QUANTIDADE_APOS_MOVIMENTACAO.intValue()))
            .andExpect(jsonPath("$.dataAtual").value(DEFAULT_DATA_ATUAL.toString()))
            .andExpect(jsonPath("$.operacao").value(DEFAULT_OPERACAO.toString()))
            .andExpect(jsonPath("$.motivo").value(DEFAULT_MOTIVO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEstoque() throws Exception {
        // Get the estoque
        restEstoqueMockMvc.perform(get("/api/estoques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstoque() throws Exception {
        // Initialize the database
        estoqueRepository.saveAndFlush(estoque);

		int databaseSizeBeforeUpdate = estoqueRepository.findAll().size();

        // Update the estoque
        estoque.setQuantidadeAtual(UPDATED_QUANTIDADE_ATUAL);
        estoque.setQuantidadeAposMovimentacao(UPDATED_QUANTIDADE_APOS_MOVIMENTACAO);
        estoque.setDataAtual(UPDATED_DATA_ATUAL);
        estoque.setOperacao(UPDATED_OPERACAO);
        estoque.setMotivo(UPDATED_MOTIVO);

        restEstoqueMockMvc.perform(put("/api/estoques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estoque)))
                .andExpect(status().isOk());

        // Validate the Estoque in the database
        List<Estoque> estoques = estoqueRepository.findAll();
        assertThat(estoques).hasSize(databaseSizeBeforeUpdate);
        Estoque testEstoque = estoques.get(estoques.size() - 1);
        assertThat(testEstoque.getQuantidadeAtual()).isEqualTo(UPDATED_QUANTIDADE_ATUAL);
        assertThat(testEstoque.getQuantidadeAposMovimentacao()).isEqualTo(UPDATED_QUANTIDADE_APOS_MOVIMENTACAO);
        assertThat(testEstoque.getDataAtual()).isEqualTo(UPDATED_DATA_ATUAL);
        assertThat(testEstoque.getOperacao()).isEqualTo(UPDATED_OPERACAO);
        assertThat(testEstoque.getMotivo()).isEqualTo(UPDATED_MOTIVO);
    }

    @Test
    @Transactional
    public void deleteEstoque() throws Exception {
        // Initialize the database
        estoqueRepository.saveAndFlush(estoque);

		int databaseSizeBeforeDelete = estoqueRepository.findAll().size();

        // Get the estoque
        restEstoqueMockMvc.perform(delete("/api/estoques/{id}", estoque.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Estoque> estoques = estoqueRepository.findAll();
        assertThat(estoques).hasSize(databaseSizeBeforeDelete - 1);
    }
}
