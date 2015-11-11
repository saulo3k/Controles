package br.com.rexapps.controles.web.rest;

import br.com.rexapps.controles.Application;
import br.com.rexapps.controles.domain.Cliente;
import br.com.rexapps.controles.repository.ClienteRepository;
import br.com.rexapps.controles.repository.search.ClienteSearchRepository;

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

import br.com.rexapps.controles.domain.enumeration.TipoTelefone;
import br.com.rexapps.controles.domain.enumeration.Estado;

/**
 * Test class for the ClienteResource REST controller.
 *
 * @see ClienteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ClienteResourceTest {

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";


private static final TipoTelefone DEFAULT_TIPO_TELEFONE = TipoTelefone.celular;
    private static final TipoTelefone UPDATED_TIPO_TELEFONE = TipoTelefone.residencial;
    private static final String DEFAULT_TELEFONE = "AAAAA";
    private static final String UPDATED_TELEFONE = "BBBBB";
    private static final String DEFAULT_CPF_CNPJ = "AAAAA";
    private static final String UPDATED_CPF_CNPJ = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_CEP = "AAAAA";
    private static final String UPDATED_CEP = "BBBBB";
    private static final String DEFAULT_ENDERECO = "AAAAA";
    private static final String UPDATED_ENDERECO = "BBBBB";


private static final Estado DEFAULT_ESTADO = Estado.AC;
    private static final Estado UPDATED_ESTADO = Estado.AL;
    private static final String DEFAULT_CIDADE = "AAAAA";
    private static final String UPDATED_CIDADE = "BBBBB";
    private static final String DEFAULT_NOME_CONTATO = "AAAAA";
    private static final String UPDATED_NOME_CONTATO = "BBBBB";
    private static final String DEFAULT_INFORMACOES_PARA_BUSCA = "AAAAA";
    private static final String UPDATED_INFORMACOES_PARA_BUSCA = "BBBBB";

    @Inject
    private ClienteRepository clienteRepository;

    @Inject
    private ClienteSearchRepository clienteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClienteMockMvc;

    private Cliente cliente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClienteResource clienteResource = new ClienteResource();
        ReflectionTestUtils.setField(clienteResource, "clienteRepository", clienteRepository);
        ReflectionTestUtils.setField(clienteResource, "clienteSearchRepository", clienteSearchRepository);
        this.restClienteMockMvc = MockMvcBuilders.standaloneSetup(clienteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        cliente = new Cliente();
        cliente.setNome(DEFAULT_NOME);
        cliente.setTipoTelefone(DEFAULT_TIPO_TELEFONE);
        cliente.setTelefone(DEFAULT_TELEFONE);
        cliente.setCpfCnpj(DEFAULT_CPF_CNPJ);
        cliente.setEmail(DEFAULT_EMAIL);
        cliente.setCep(DEFAULT_CEP);
        cliente.setEndereco(DEFAULT_ENDERECO);
        cliente.setEstado(DEFAULT_ESTADO);
        cliente.setCidade(DEFAULT_CIDADE);
        cliente.setNomeContato(DEFAULT_NOME_CONTATO);
        cliente.setInformacoesParaBusca(DEFAULT_INFORMACOES_PARA_BUSCA);
    }

    @Test
    @Transactional
    public void createCliente() throws Exception {
        int databaseSizeBeforeCreate = clienteRepository.findAll().size();

        // Create the Cliente

        restClienteMockMvc.perform(post("/api/clientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cliente)))
                .andExpect(status().isCreated());

        // Validate the Cliente in the database
        List<Cliente> clientes = clienteRepository.findAll();
        assertThat(clientes).hasSize(databaseSizeBeforeCreate + 1);
        Cliente testCliente = clientes.get(clientes.size() - 1);
        assertThat(testCliente.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCliente.getTipoTelefone()).isEqualTo(DEFAULT_TIPO_TELEFONE);
        assertThat(testCliente.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
        assertThat(testCliente.getCpfCnpj()).isEqualTo(DEFAULT_CPF_CNPJ);
        assertThat(testCliente.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCliente.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testCliente.getEndereco()).isEqualTo(DEFAULT_ENDERECO);
        assertThat(testCliente.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testCliente.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testCliente.getNomeContato()).isEqualTo(DEFAULT_NOME_CONTATO);
        assertThat(testCliente.getInformacoesParaBusca()).isEqualTo(DEFAULT_INFORMACOES_PARA_BUSCA);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteRepository.findAll().size();
        // set the field null
        cliente.setNome(null);

        // Create the Cliente, which fails.

        restClienteMockMvc.perform(post("/api/clientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cliente)))
                .andExpect(status().isBadRequest());

        List<Cliente> clientes = clienteRepository.findAll();
        assertThat(clientes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClientes() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clientes
        restClienteMockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].tipoTelefone").value(hasItem(DEFAULT_TIPO_TELEFONE.toString())))
                .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE.toString())))
                .andExpect(jsonPath("$.[*].cpfCnpj").value(hasItem(DEFAULT_CPF_CNPJ.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
                .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO.toString())))
                .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
                .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
                .andExpect(jsonPath("$.[*].nomeContato").value(hasItem(DEFAULT_NOME_CONTATO.toString())))
                .andExpect(jsonPath("$.[*].informacoesParaBusca").value(hasItem(DEFAULT_INFORMACOES_PARA_BUSCA.toString())));
    }

    @Test
    @Transactional
    public void getCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get the cliente
        restClienteMockMvc.perform(get("/api/clientes/{id}", cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cliente.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.tipoTelefone").value(DEFAULT_TIPO_TELEFONE.toString()))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE.toString()))
            .andExpect(jsonPath("$.cpfCnpj").value(DEFAULT_CPF_CNPJ.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP.toString()))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE.toString()))
            .andExpect(jsonPath("$.nomeContato").value(DEFAULT_NOME_CONTATO.toString()))
            .andExpect(jsonPath("$.informacoesParaBusca").value(DEFAULT_INFORMACOES_PARA_BUSCA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCliente() throws Exception {
        // Get the cliente
        restClienteMockMvc.perform(get("/api/clientes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

		int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente
        cliente.setNome(UPDATED_NOME);
        cliente.setTipoTelefone(UPDATED_TIPO_TELEFONE);
        cliente.setTelefone(UPDATED_TELEFONE);
        cliente.setCpfCnpj(UPDATED_CPF_CNPJ);
        cliente.setEmail(UPDATED_EMAIL);
        cliente.setCep(UPDATED_CEP);
        cliente.setEndereco(UPDATED_ENDERECO);
        cliente.setEstado(UPDATED_ESTADO);
        cliente.setCidade(UPDATED_CIDADE);
        cliente.setNomeContato(UPDATED_NOME_CONTATO);
        cliente.setInformacoesParaBusca(UPDATED_INFORMACOES_PARA_BUSCA);

        restClienteMockMvc.perform(put("/api/clientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cliente)))
                .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clientes = clienteRepository.findAll();
        assertThat(clientes).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clientes.get(clientes.size() - 1);
        assertThat(testCliente.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCliente.getTipoTelefone()).isEqualTo(UPDATED_TIPO_TELEFONE);
        assertThat(testCliente.getTelefone()).isEqualTo(UPDATED_TELEFONE);
        assertThat(testCliente.getCpfCnpj()).isEqualTo(UPDATED_CPF_CNPJ);
        assertThat(testCliente.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCliente.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testCliente.getEndereco()).isEqualTo(UPDATED_ENDERECO);
        assertThat(testCliente.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testCliente.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testCliente.getNomeContato()).isEqualTo(UPDATED_NOME_CONTATO);
        assertThat(testCliente.getInformacoesParaBusca()).isEqualTo(UPDATED_INFORMACOES_PARA_BUSCA);
    }

    @Test
    @Transactional
    public void deleteCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

		int databaseSizeBeforeDelete = clienteRepository.findAll().size();

        // Get the cliente
        restClienteMockMvc.perform(delete("/api/clientes/{id}", cliente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Cliente> clientes = clienteRepository.findAll();
        assertThat(clientes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
