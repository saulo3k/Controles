package br.com.rexapps.controles.web.rest;

import br.com.rexapps.controles.Application;
import br.com.rexapps.controles.domain.Produto;
import br.com.rexapps.controles.repository.ProdutoRepository;
import br.com.rexapps.controles.repository.search.ProdutoSearchRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.rexapps.controles.domain.enumeration.UnidadeMedida;

/**
 * Test class for the ProdutoResource REST controller.
 *
 * @see ProdutoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProdutoResourceTest {

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";
    private static final String DEFAULT_DESCRICAO = "AAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBB";
    private static final String DEFAULT_REFERENCIA = "AAAAA";
    private static final String UPDATED_REFERENCIA = "BBBBB";

    private static final Long DEFAULT_CODIGO_BARRAS = 1L;
    private static final Long UPDATED_CODIGO_BARRAS = 2L;

    private static final BigDecimal DEFAULT_PRECO_CUSTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECO_CUSTO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PRECO_VENDA = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECO_VENDA = new BigDecimal(2);

    private static final Long DEFAULT_ESTOQUE = 1L;
    private static final Long UPDATED_ESTOQUE = 2L;

    private static final Boolean DEFAULT_VENDA_SEM_ESTOQUE = false;
    private static final Boolean UPDATED_VENDA_SEM_ESTOQUE = true;

    private static final Boolean DEFAULT_PROMOCAO = false;
    private static final Boolean UPDATED_PROMOCAO = true;

    private static final LocalDate DEFAULT_DATA_CADASTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CADASTRO = LocalDate.now(ZoneId.systemDefault());


private static final UnidadeMedida DEFAULT_UNIDADE_MEDIDA = UnidadeMedida.G;
    private static final UnidadeMedida UPDATED_UNIDADE_MEDIDA = UnidadeMedida.KG;

    @Inject
    private ProdutoRepository produtoRepository;

    @Inject
    private ProdutoSearchRepository produtoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProdutoMockMvc;

    private Produto produto;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProdutoResource produtoResource = new ProdutoResource();
        ReflectionTestUtils.setField(produtoResource, "produtoRepository", produtoRepository);
        ReflectionTestUtils.setField(produtoResource, "produtoSearchRepository", produtoSearchRepository);
        this.restProdutoMockMvc = MockMvcBuilders.standaloneSetup(produtoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        produto = new Produto();
        produto.setNome(DEFAULT_NOME);
        produto.setDescricao(DEFAULT_DESCRICAO);
        produto.setReferencia(DEFAULT_REFERENCIA);
        produto.setCodigoBarras(DEFAULT_CODIGO_BARRAS);
        produto.setPrecoCusto(DEFAULT_PRECO_CUSTO);
        produto.setPrecoVenda(DEFAULT_PRECO_VENDA);
        produto.setEstoque(DEFAULT_ESTOQUE);
        produto.setVendaSemEstoque(DEFAULT_VENDA_SEM_ESTOQUE);
        produto.setPromocao(DEFAULT_PROMOCAO);
        produto.setDataCadastro(DEFAULT_DATA_CADASTRO);
        produto.setUnidadeMedida(DEFAULT_UNIDADE_MEDIDA);
    }

    @Test
    @Transactional
    public void createProduto() throws Exception {
        int databaseSizeBeforeCreate = produtoRepository.findAll().size();

        // Create the Produto

        restProdutoMockMvc.perform(post("/api/produtos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produto)))
                .andExpect(status().isCreated());

        // Validate the Produto in the database
        List<Produto> produtos = produtoRepository.findAll();
        assertThat(produtos).hasSize(databaseSizeBeforeCreate + 1);
        Produto testProduto = produtos.get(produtos.size() - 1);
        assertThat(testProduto.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testProduto.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
        assertThat(testProduto.getReferencia()).isEqualTo(DEFAULT_REFERENCIA);
        assertThat(testProduto.getCodigoBarras()).isEqualTo(DEFAULT_CODIGO_BARRAS);
        assertThat(testProduto.getPrecoCusto()).isEqualTo(DEFAULT_PRECO_CUSTO);
        assertThat(testProduto.getPrecoVenda()).isEqualTo(DEFAULT_PRECO_VENDA);
        assertThat(testProduto.getEstoque()).isEqualTo(DEFAULT_ESTOQUE);
        assertThat(testProduto.getVendaSemEstoque()).isEqualTo(DEFAULT_VENDA_SEM_ESTOQUE);
        assertThat(testProduto.getPromocao()).isEqualTo(DEFAULT_PROMOCAO);
        assertThat(testProduto.getDataCadastro()).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testProduto.getUnidadeMedida()).isEqualTo(DEFAULT_UNIDADE_MEDIDA);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = produtoRepository.findAll().size();
        // set the field null
        produto.setNome(null);

        // Create the Produto, which fails.

        restProdutoMockMvc.perform(post("/api/produtos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produto)))
                .andExpect(status().isBadRequest());

        List<Produto> produtos = produtoRepository.findAll();
        assertThat(produtos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrecoCustoIsRequired() throws Exception {
        int databaseSizeBeforeTest = produtoRepository.findAll().size();
        // set the field null
        produto.setPrecoCusto(null);

        // Create the Produto, which fails.

        restProdutoMockMvc.perform(post("/api/produtos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produto)))
                .andExpect(status().isBadRequest());

        List<Produto> produtos = produtoRepository.findAll();
        assertThat(produtos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProdutos() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get all the produtos
        restProdutoMockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(produto.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())))
                .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA.toString())))
                .andExpect(jsonPath("$.[*].codigoBarras").value(hasItem(DEFAULT_CODIGO_BARRAS.intValue())))
                .andExpect(jsonPath("$.[*].precoCusto").value(hasItem(DEFAULT_PRECO_CUSTO.intValue())))
                .andExpect(jsonPath("$.[*].precoVenda").value(hasItem(DEFAULT_PRECO_VENDA.intValue())))
                .andExpect(jsonPath("$.[*].estoque").value(hasItem(DEFAULT_ESTOQUE.intValue())))
                .andExpect(jsonPath("$.[*].vendaSemEstoque").value(hasItem(DEFAULT_VENDA_SEM_ESTOQUE.booleanValue())))
                .andExpect(jsonPath("$.[*].promocao").value(hasItem(DEFAULT_PROMOCAO.booleanValue())))
                .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(DEFAULT_DATA_CADASTRO.toString())))
                .andExpect(jsonPath("$.[*].unidadeMedida").value(hasItem(DEFAULT_UNIDADE_MEDIDA.toString())));
    }

    @Test
    @Transactional
    public void getProduto() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

        // Get the produto
        restProdutoMockMvc.perform(get("/api/produtos/{id}", produto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(produto.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()))
            .andExpect(jsonPath("$.referencia").value(DEFAULT_REFERENCIA.toString()))
            .andExpect(jsonPath("$.codigoBarras").value(DEFAULT_CODIGO_BARRAS.intValue()))
            .andExpect(jsonPath("$.precoCusto").value(DEFAULT_PRECO_CUSTO.intValue()))
            .andExpect(jsonPath("$.precoVenda").value(DEFAULT_PRECO_VENDA.intValue()))
            .andExpect(jsonPath("$.estoque").value(DEFAULT_ESTOQUE.intValue()))
            .andExpect(jsonPath("$.vendaSemEstoque").value(DEFAULT_VENDA_SEM_ESTOQUE.booleanValue()))
            .andExpect(jsonPath("$.promocao").value(DEFAULT_PROMOCAO.booleanValue()))
            .andExpect(jsonPath("$.dataCadastro").value(DEFAULT_DATA_CADASTRO.toString()))
            .andExpect(jsonPath("$.unidadeMedida").value(DEFAULT_UNIDADE_MEDIDA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProduto() throws Exception {
        // Get the produto
        restProdutoMockMvc.perform(get("/api/produtos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduto() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

		int databaseSizeBeforeUpdate = produtoRepository.findAll().size();

        // Update the produto
        produto.setNome(UPDATED_NOME);
        produto.setDescricao(UPDATED_DESCRICAO);
        produto.setReferencia(UPDATED_REFERENCIA);
        produto.setCodigoBarras(UPDATED_CODIGO_BARRAS);
        produto.setPrecoCusto(UPDATED_PRECO_CUSTO);
        produto.setPrecoVenda(UPDATED_PRECO_VENDA);
        produto.setEstoque(UPDATED_ESTOQUE);
        produto.setVendaSemEstoque(UPDATED_VENDA_SEM_ESTOQUE);
        produto.setPromocao(UPDATED_PROMOCAO);
        produto.setDataCadastro(UPDATED_DATA_CADASTRO);
        produto.setUnidadeMedida(UPDATED_UNIDADE_MEDIDA);

        restProdutoMockMvc.perform(put("/api/produtos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(produto)))
                .andExpect(status().isOk());

        // Validate the Produto in the database
        List<Produto> produtos = produtoRepository.findAll();
        assertThat(produtos).hasSize(databaseSizeBeforeUpdate);
        Produto testProduto = produtos.get(produtos.size() - 1);
        assertThat(testProduto.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testProduto.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
        assertThat(testProduto.getReferencia()).isEqualTo(UPDATED_REFERENCIA);
        assertThat(testProduto.getCodigoBarras()).isEqualTo(UPDATED_CODIGO_BARRAS);
        assertThat(testProduto.getPrecoCusto()).isEqualTo(UPDATED_PRECO_CUSTO);
        assertThat(testProduto.getPrecoVenda()).isEqualTo(UPDATED_PRECO_VENDA);
        assertThat(testProduto.getEstoque()).isEqualTo(UPDATED_ESTOQUE);
        assertThat(testProduto.getVendaSemEstoque()).isEqualTo(UPDATED_VENDA_SEM_ESTOQUE);
        assertThat(testProduto.getPromocao()).isEqualTo(UPDATED_PROMOCAO);
        assertThat(testProduto.getDataCadastro()).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testProduto.getUnidadeMedida()).isEqualTo(UPDATED_UNIDADE_MEDIDA);
    }

    @Test
    @Transactional
    public void deleteProduto() throws Exception {
        // Initialize the database
        produtoRepository.saveAndFlush(produto);

		int databaseSizeBeforeDelete = produtoRepository.findAll().size();

        // Get the produto
        restProdutoMockMvc.perform(delete("/api/produtos/{id}", produto.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Produto> produtos = produtoRepository.findAll();
        assertThat(produtos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
