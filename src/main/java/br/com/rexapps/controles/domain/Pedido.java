package br.com.rexapps.controles.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import br.com.rexapps.controles.domain.enumeration.DiaSemana;
import br.com.rexapps.controles.domain.enumeration.StatusPedido;

/**
 * A Pedido.
 */
@Entity
@Table(name = "pedido")
@Document(indexName = "pedido")
public class Pedido implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "dt_prevista_separacao")
    private LocalDate dtPrevistaSeparacao;

    @NotNull
    @Column(name = "dt_real_separacao", nullable = false)
    private LocalDate dtRealSeparacao;

    @Column(name = "dt_prevista_entrega", nullable = false)
    private LocalDate dtPrevistaEntrega;

    @Column(name = "dt_real_entrega", nullable = false)
    private LocalDate dtRealEntrega;

    @Column(name = "periodo_pedido_inicio", nullable = false)
    private LocalDate periodoPedidoInicio;

    @Column(name = "periodo_pedido_fim", nullable = false)
    private LocalDate periodoPedidoFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana")
    private DiaSemana diaSemana;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido")
    private StatusPedido statusPedido;
      
	@Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @Column(name = "usuario_que_fez_pedido")
    private Integer usuarioQueFezPedido;
    
    

    @OneToMany(mappedBy = "pedido")
    @JsonIgnore
    private Set<Produto> produtos = new HashSet<>();

    @ManyToOne
    private User user;

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

    public StatusPedido getStatusPedido() {
		return statusPedido;
	}

	public void setStatusPedido(StatusPedido statusPedido) {
		this.statusPedido = statusPedido;
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

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDate dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Integer getUsuarioQueFezPedido() {
        return usuarioQueFezPedido;
    }

    public void setUsuarioQueFezPedido(Integer usuarioQueFezPedido) {
        this.usuarioQueFezPedido = usuarioQueFezPedido;
    }

    public Set<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<Produto> produtos) {
        this.produtos = produtos;
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
            ", diaSemana='" + diaSemana + "'" +
            ", dataPedido='" + dataPedido + "'" +
            ", usuarioQueFezPedido='" + usuarioQueFezPedido + "'" +
            '}';
    }
}
