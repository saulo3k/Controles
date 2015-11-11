package br.com.rexapps.controles.web.rest;

import br.com.rexapps.controles.Application;
import br.com.rexapps.controles.domain.CategoriaProduto;
import br.com.rexapps.controles.repository.CategoriaProdutoRepository;
import br.com.rexapps.controles.repository.search.CategoriaProdutoSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CategoriaProdutoResource REST controller.
 *
 * @see CategoriaProdutoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CategoriaProdutoResourceTest {

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";
    private static final String DEFAULT_DESCRICAO = "AAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBB";

    private static final Boolean DEFAULT_ATIVA = false;
    private static final Boolean UPDATED_ATIVA = true;

    @Inject
    private CategoriaProdutoRepository categoriaProdutoRepository;

    @Inject
    private CategoriaProdutoSearchRepository categoriaProdutoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCategoriaProdutoMockMvc;

    private CategoriaProduto categoriaProduto;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CategoriaProdutoResource categoriaProdutoResource = new CategoriaProdutoResource();
        ReflectionTestUtils.setField(categoriaProdutoResource, "categoriaProdutoRepository", categoriaProdutoRepository);
        ReflectionTestUtils.setField(categoriaProdutoResource, "categoriaProdutoSearchRepository", categoriaProdutoSearchRepository);
        this.restCategoriaProdutoMockMvc = MockMvcBuilders.standaloneSetup(categoriaProdutoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        categoriaProduto = new CategoriaProduto();
        categoriaProduto.setNome(DEFAULT_NOME);
        categoriaProduto.setDescricao(DEFAULT_DESCRICAO);
        categoriaProduto.setAtiva(DEFAULT_ATIVA);
    }

    @Test
    @Transactional
    public void createCategoriaProduto() throws Exception {
        int databaseSizeBeforeCreate = categoriaProdutoRepository.findAll().size();

        // Create the CategoriaProduto

        restCategoriaProdutoMockMvc.perform(post("/api/categoriaProdutos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categoriaProduto)))
                .andExpect(status().isCreated());

        // Validate the CategoriaProduto in the database
        List<CategoriaProduto> categoriaProdutos = categoriaProdutoRepository.findAll();
        assertThat(categoriaProdutos).hasSize(databaseSizeBeforeCreate + 1);
        CategoriaProduto testCategoriaProduto = categoriaProdutos.get(categoriaProdutos.size() - 1);
        assertThat(testCategoriaProduto.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCategoriaProduto.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testCategoriaProduto.getAtiva()).isEqualTo(DEFAULT_ATIVA);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoriaProdutoRepository.findAll().size();
        // set the field null
        categoriaProduto.setNome(null);

        // Create the CategoriaProduto, which fails.

        restCategoriaProdutoMockMvc.perform(post("/api/categoriaProdutos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categoriaProduto)))
                .andExpect(status().isBadRequest());

        List<CategoriaProduto> categoriaProdutos = categoriaProdutoRepository.findAll();
        assertThat(categoriaProdutos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoriaProdutoRepository.findAll().size();
        // set the field null
        categoriaProduto.setDescricao(null);

        // Create the CategoriaProduto, which fails.

        restCategoriaProdutoMockMvc.perform(post("/api/categoriaProdutos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categoriaProduto)))
                .andExpect(status().isBadRequest());

        List<CategoriaProduto> categoriaProdutos = categoriaProdutoRepository.findAll();
        assertThat(categoriaProdutos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAtivaIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoriaProdutoRepository.findAll().size();
        // set the field null
        categoriaProduto.setAtiva(null);

        // Create the CategoriaProduto, which fails.

        restCategoriaProdutoMockMvc.perform(post("/api/categoriaProdutos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categoriaProduto)))
                .andExpect(status().isBadRequest());

        List<CategoriaProduto> categoriaProdutos = categoriaProdutoRepository.findAll();
        assertThat(categoriaProdutos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategoriaProdutos() throws Exception {
        // Initialize the database
        categoriaProdutoRepository.saveAndFlush(categoriaProduto);

        // Get all the categoriaProdutos
        restCategoriaProdutoMockMvc.perform(get("/api/categoriaProdutos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(categoriaProduto.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
                .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA.booleanValue())));
    }

    @Test
    @Transactional
    public void getCategoriaProduto() throws Exception {
        // Initialize the database
        categoriaProdutoRepository.saveAndFlush(categoriaProduto);

        // Get the categoriaProduto
        restCategoriaProdutoMockMvc.perform(get("/api/categoriaProdutos/{id}", categoriaProduto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(categoriaProduto.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.ativa").value(DEFAULT_ATIVA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCategoriaProduto() throws Exception {
        // Get the categoriaProduto
        restCategoriaProdutoMockMvc.perform(get("/api/categoriaProdutos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategoriaProduto() throws Exception {
        // Initialize the database
        categoriaProdutoRepository.saveAndFlush(categoriaProduto);

		int databaseSizeBeforeUpdate = categoriaProdutoRepository.findAll().size();

        // Update the categoriaProduto
        categoriaProduto.setNome(UPDATED_NOME);
        categoriaProduto.setDescricao(UPDATED_DESCRICAO);
        categoriaProduto.setAtiva(UPDATED_ATIVA);

        restCategoriaProdutoMockMvc.perform(put("/api/categoriaProdutos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(categoriaProduto)))
                .andExpect(status().isOk());

        // Validate the CategoriaProduto in the database
        List<CategoriaProduto> categoriaProdutos = categoriaProdutoRepository.findAll();
        assertThat(categoriaProdutos).hasSize(databaseSizeBeforeUpdate);
        CategoriaProduto testCategoriaProduto = categoriaProdutos.get(categoriaProdutos.size() - 1);
        assertThat(testCategoriaProduto.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCategoriaProduto.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testCategoriaProduto.getAtiva()).isEqualTo(UPDATED_ATIVA);
    }

    @Test
    @Transactional
    public void deleteCategoriaProduto() throws Exception {
        // Initialize the database
        categoriaProdutoRepository.saveAndFlush(categoriaProduto);

		int databaseSizeBeforeDelete = categoriaProdutoRepository.findAll().size();

        // Get the categoriaProduto
        restCategoriaProdutoMockMvc.perform(delete("/api/categoriaProdutos/{id}", categoriaProduto.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CategoriaProduto> categoriaProdutos = categoriaProdutoRepository.findAll();
        assertThat(categoriaProdutos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
