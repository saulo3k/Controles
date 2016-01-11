package br.com.rexapps.controles.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.elasticsearch.annotations.Document;

import br.com.rexapps.controles.domain.enumeration.OperacaoEstoque;

/**
 * A Estoque.
 */
@Entity
@Table(name = "estoque")
@Document(indexName = "estoque")
public class Estoque implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "quantidade_atual")
    private Long quantidadeAtual;

    @Column(name = "quantidade_apos_movimentacao")
    private Long quantidadeAposMovimentacao;

    @Column(name = "data_atual")
    private LocalDate dataAtual;

    @Enumerated(EnumType.STRING)
    @Column(name = "operacao")
    private OperacaoEstoque operacao;
    
    @Column(name = "quantidade")
    private Long quantidade;
    
    public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	@Column(name = "motivo")
    private String motivo;

    @ManyToOne
    private Produto estoque_produto;

    @ManyToOne
    private User estoque_user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(Long quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public Long getQuantidadeAposMovimentacao() {
        return quantidadeAposMovimentacao;
    }

    public void setQuantidadeAposMovimentacao(Long quantidadeAposMovimentacao) {
        this.quantidadeAposMovimentacao = quantidadeAposMovimentacao;
    }

    public LocalDate getDataAtual() {
        return dataAtual;
    }

    public void setDataAtual(LocalDate dataAtual) {
        this.dataAtual = dataAtual;
    }

    public OperacaoEstoque getOperacao() {
        return operacao;
    }

    public void setOperacao(OperacaoEstoque operacao) {
        this.operacao = operacao;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Produto getEstoque_produto() {
        return estoque_produto;
    }

    public void setEstoque_produto(Produto produto) {
        this.estoque_produto = produto;
    }

    public User getEstoque_user() {
        return estoque_user;
    }

    public void setEstoque_user(User user) {
        this.estoque_user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Estoque estoque = (Estoque) o;

        if ( ! Objects.equals(id, estoque.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Estoque{" +
            "id=" + id +
            ", quantidadeAtual='" + quantidadeAtual + "'" +
            ", quantidadeAposMovimentacao='" + quantidadeAposMovimentacao + "'" +
            ", dataAtual='" + dataAtual + "'" +
            ", operacao='" + operacao + "'" +
            ", motivo='" + motivo + "'" +
            '}';
    }
}
