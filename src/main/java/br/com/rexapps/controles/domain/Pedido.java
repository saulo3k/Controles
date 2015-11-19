package br.com.rexapps.controles.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.rexapps.controles.domain.enumeration.StatusPedido;

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
            
    @OneToMany(mappedBy="pedido", cascade=CascadeType.ALL)
    private Set<ProdutosPedidos> produtosPedidos = new HashSet<>();
    
	@ManyToOne
    private User user_pedido;

    @ManyToOne
    private Cliente cliente_pedido;
    
    @Enumerated(EnumType.ORDINAL)   
    @Column(name = "status_pedido")
    private StatusPedido statusPedido;
    
    @Column(name = "pedido_modelo")
    private boolean pedidoModelo;

    
	public boolean isPedidoModelo() {
		return pedidoModelo;
	}

	public void setPedidoModelo(boolean pedidoModelo) {
		this.pedidoModelo = pedidoModelo;
	}

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

//	public Set<Produto> getProduto_has_pedidos() {
//        return produto_has_pedidos;
//    }
//
//    public void setProduto_has_pedidos(Set<Produto> produtos) {
//        this.produto_has_pedidos = produtos;
//    }

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
	
	public Set<ProdutosPedidos> getProdutosPedidos() {
		return produtosPedidos;
	}

	public void setProdutosPedidos(Set<ProdutosPedidos> produtosPedidos) {
		this.produtosPedidos = produtosPedidos;
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
