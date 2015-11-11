package br.com.rexapps.controles.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.elasticsearch.annotations.Document;

import br.com.rexapps.controles.domain.enumeration.UnidadeMedida;

/**
 * A Produto.
 */
@Entity
@Table(name = "produto")
@Document(indexName = "produto")
public class Produto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Size(max = 255)
    @Column(name = "referencia", length = 255)
    private String referencia;

    @Column(name = "codigo_barras")
    private Long codigoBarras;

    @NotNull
    @Column(name = "preco_custo", precision=10, scale=2, nullable = false)
    private BigDecimal precoCusto;

    @Column(name = "preco_venda", precision=10, scale=2, nullable = false)
    private BigDecimal precoVenda;

    @Column(name = "estoque")
    private Long estoque;

    @Column(name = "venda_sem_estoque")
    private Boolean vendaSemEstoque;

    @Column(name = "promocao")
    private Boolean promocao;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    @Enumerated(EnumType.STRING)
    @Column(name = "unidade_medida")
    private UnidadeMedida unidadeMedida;

    @ManyToOne
    private CategoriaProduto categoriaProduto;

    @ManyToOne
    private User user;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Long getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(Long codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Long getEstoque() {
        return estoque;
    }

    public void setEstoque(Long estoque) {
        this.estoque = estoque;
    }

    public Boolean getVendaSemEstoque() {
        return vendaSemEstoque;
    }

    public void setVendaSemEstoque(Boolean vendaSemEstoque) {
        this.vendaSemEstoque = vendaSemEstoque;
    }

    public Boolean getPromocao() {
        return promocao;
    }

    public void setPromocao(Boolean promocao) {
        this.promocao = promocao;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public CategoriaProduto getCategoriaProduto() {
        return categoriaProduto;
    }

    public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Produto produto = (Produto) o;

        if ( ! Objects.equals(id, produto.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Produto{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", descricao='" + descricao + "'" +
            ", referencia='" + referencia + "'" +
            ", codigoBarras='" + codigoBarras + "'" +
            ", precoCusto='" + precoCusto + "'" +
            ", precoVenda='" + precoVenda + "'" +
            ", estoque='" + estoque + "'" +
            ", vendaSemEstoque='" + vendaSemEstoque + "'" +
            ", promocao='" + promocao + "'" +
            ", dataCadastro='" + dataCadastro + "'" +
            ", unidadeMedida='" + unidadeMedida + "'" +
            '}';
    }
}
