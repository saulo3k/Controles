package br.com.rexapps.controles.domain;

import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.rexapps.controles.domain.enumeration.StatusPedido;
import br.com.rexapps.controles.domain.enumeration.UnidadeMedida;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Pedido.
 */
@Entity
@Table(name = "pedido")
@Document(indexName = "pedido")
@JsonIgnoreProperties( ignoreUnknown = true )
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "dt_prevista_separacao")
    private LocalDate dtPrevistaSeparacao;

    @Column(name = "dt_real_separacao")
    private LocalDate dtRealSeparacao;

    @Column(name = "dt_prevista_entrega")
    private LocalDate dtPrevistaEntrega;

    @Column(name = "dt_real_entrega")
    private LocalDate dtRealEntrega;

    @Column(name = "periodo_pedido_inicio")
    private LocalDate periodoPedidoInicio;

    @Column(name = "periodo_pedido_fim")
    private LocalDate periodoPedidoFim;

    @Column(name = "data_pedido")
    private LocalDate dataPedido;

    @ManyToMany    @JoinTable(name = "pedido_produto_has_pedido",
               joinColumns = @JoinColumn(name="pedidos_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="produto_has_pedidos_id", referencedColumnName="ID"))
    private Set<Produto> produto_has_pedidos = new HashSet<>();

    @ManyToOne
    private User user_pedido;

    @ManyToOne
    private Cliente cliente_pedido;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "modelo_pedido")
    private StatusPedido statusPedido;

    
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDate getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(LocalDate dataPedido) {
		this.dataPedido = dataPedido;
	}

	public Set<Produto> getProduto_has_pedidos() {
        return produto_has_pedidos;
    }

    public void setProduto_has_pedidos(Set<Produto> produtos) {
        this.produto_has_pedidos = produtos;
    }

    public User getUser_pedido() {
        return user_pedido;
    }

    public void setUser_pedido(User user) {
        this.user_pedido = user;
    }

    public Cliente getCliente_pedido() {
        return cliente_pedido;
    }

    public void setCliente_pedido(Cliente cliente) {
        this.cliente_pedido = cliente;
    }

    public StatusPedido getStatusPedido() {
		return statusPedido;
	}

	public void setStatusPedido(StatusPedido statusPedido) {
		this.statusPedido = statusPedido;
	}

	
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pedido pedido = (Pedido) o;

        if ( ! Objects.equals(id, pedido.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pedido{" +
            "id=" + id +
            ", dtPrevistaSeparacao='" + dtPrevistaSeparacao + "'" +
            ", dtRealSeparacao='" + dtRealSeparacao + "'" +
            ", dtPrevistaEntrega='" + dtPrevistaEntrega + "'" +
            ", dtRealEntrega='" + dtRealEntrega + "'" +
            ", periodoPedidoInicio='" + periodoPedidoInicio + "'" +
            ", periodoPedidoFim='" + periodoPedidoFim + "'" +
            ", dataPedido='" + dataPedido + "'" +
            '}';
    }
}
