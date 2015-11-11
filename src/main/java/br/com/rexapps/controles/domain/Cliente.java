package br.com.rexapps.controles.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

import br.com.rexapps.controles.domain.enumeration.TipoTelefone;

import br.com.rexapps.controles.domain.enumeration.Estado;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@Document(indexName = "cliente")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_telefone")
    private TipoTelefone tipoTelefone;

    @Pattern(regexp = "^[1-9]{2}\\-[2-9][0-9]{7,8}$")
    @Column(name = "telefone")
    private String telefone;

    @Size(max = 255)
    @Column(name = "cpf_cnpj", length = 255)
    private String cpfCnpj;

    @Pattern(regexp = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")
    @Column(name = "email")
    private String email;

    @Column(name = "cep")
    private String cep;

    @Column(name = "endereco")
    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private Estado estado;

    @Column(name = "cidade")
    private String cidade;

    @Column(name = "nome_contato")
    private String nomeContato;

    @Column(name = "informacoes_para_busca")
    private String informacoesParaBusca;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoTelefone getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(TipoTelefone tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getNomeContato() {
        return nomeContato;
    }

    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    public String getInformacoesParaBusca() {
        return informacoesParaBusca;
    }

    public void setInformacoesParaBusca(String informacoesParaBusca) {
        this.informacoesParaBusca = informacoesParaBusca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Cliente cliente = (Cliente) o;

        if ( ! Objects.equals(id, cliente.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", tipoTelefone='" + tipoTelefone + "'" +
            ", telefone='" + telefone + "'" +
            ", cpfCnpj='" + cpfCnpj + "'" +
            ", email='" + email + "'" +
            ", cep='" + cep + "'" +
            ", endereco='" + endereco + "'" +
            ", estado='" + estado + "'" +
            ", cidade='" + cidade + "'" +
            ", nomeContato='" + nomeContato + "'" +
            ", informacoesParaBusca='" + informacoesParaBusca + "'" +
            '}';
    }
}
