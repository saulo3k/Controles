package br.com.rexapps.controles.domain;

import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Ordem.
 */
@Entity
@Table(name = "ordem")
@Document(indexName = "ordem")
public class Ordem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "periodo_pedido_inicio")
    private LocalDate periodoPedidoInicio;

    @Column(name = "periodo_pedido_fim")
    private LocalDate periodoPedidoFim;

    @Column(name = "dt_prevista_separacao")
    private LocalDate dtPrevistaSeparacao;

    @Column(name = "dt_real_separacao")
    private LocalDate dtRealSeparacao;

    @Column(name = "dt_prevista_entrega")
    private LocalDate dtPrevistaEntrega;

    @Column(name = "dt_real_entrega")
    private LocalDate dtRealEntrega;

    @Column(name = "data_pedido")
    private LocalDate dataPedido;

    @ManyToMany    @JoinTable(name = "ordem_ordem_produto",
               joinColumns = @JoinColumn(name="ordems_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="ordem_produtos_id", referencedColumnName="ID"))
    private Set<Produto> ordem_produtos = new HashSet<>();

    @ManyToOne
    private Cliente ordem_cliente;

    @ManyToOne
    private User ordem_user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getPeriodoPedidoInicio() {
        return periodoPedidoInicio;
    }

    public void setPeriodoPedidoInicio(LocalDate periodoPedidoInicio) {
        this.periodoPedidoInicio = periodoPedidoInicio;
    }

    public LocalDate getPeriodoPedidoFim() {
        return periodoPedidoFim;
    }

    public void setPeriodoPedidoFim(LocalDate periodoPedidoFim) {
        this.periodoPedidoFim = periodoPedidoFim;
    }

    public LocalDate getDtPrevistaSeparacao() {
        return dtPrevistaSeparacao;
    }

    public void setDtPrevistaSeparacao(LocalDate dtPrevistaSeparacao) {
        this.dtPrevistaSeparacao = dtPrevistaSeparacao;
    }

    public LocalDate getDtRealSeparacao() {
        return dtRealSeparacao;
    }

    public void setDtRealSeparacao(LocalDate dtRealSeparacao) {
        this.dtRealSeparacao = dtRealSeparacao;
    }

    public LocalDate getDtPrevistaEntrega() {
        return dtPrevistaEntrega;
    }

    public void setDtPrevistaEntrega(LocalDate dtPrevistaEntrega) {
        this.dtPrevistaEntrega = dtPrevistaEntrega;
    }

    public LocalDate getDtRealEntrega() {
        return dtRealEntrega;
    }

    public void setDtRealEntrega(LocalDate dtRealEntrega) {
        this.dtRealEntrega = dtRealEntrega;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Set<Produto> getOrdem_produtos() {
        return ordem_produtos;
    }

    public void setOrdem_produtos(Set<Produto> produtos) {
        this.ordem_produtos = produtos;
    }

    public Cliente getOrdem_cliente() {
        return ordem_cliente;
    }

    public void setOrdem_cliente(Cliente cliente) {
        this.ordem_cliente = cliente;
    }

    public User getOrdem_user() {
        return ordem_user;
    }

    public void setOrdem_user(User user) {
        this.ordem_user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Ordem ordem = (Ordem) o;

        if ( ! Objects.equals(id, ordem.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ordem{" +
            "id=" + id +
            ", periodoPedidoInicio='" + periodoPedidoInicio + "'" +
            ", periodoPedidoFim='" + periodoPedidoFim + "'" +
            ", dtPrevistaSeparacao='" + dtPrevistaSeparacao + "'" +
            ", dtRealSeparacao='" + dtRealSeparacao + "'" +
            ", dtPrevistaEntrega='" + dtPrevistaEntrega + "'" +
            ", dtRealEntrega='" + dtRealEntrega + "'" +
            ", dataPedido='" + dataPedido + "'" +
            '}';
    }
}
