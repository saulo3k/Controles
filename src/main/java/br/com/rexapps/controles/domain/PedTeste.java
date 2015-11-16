package br.com.rexapps.controles.domain;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PedTeste.
 */
@Entity
@Table(name = "ped_teste")
@Document(indexName = "pedteste")
public class PedTeste implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "dt_prevista_entrega")
    private LocalDate dtPrevistaEntrega;

    @Column(name = "dt_prevista_entrega_zoned")
    private ZonedDateTime dtPrevistaEntregaZoned;

    @ManyToMany    @JoinTable(name = "ped_teste_produtoped",
               joinColumns = @JoinColumn(name="ped_testes_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="produtopeds_id", referencedColumnName="ID"))
    private Set<Produto> produtopeds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDtPrevistaEntrega() {
        return dtPrevistaEntrega;
    }

    public void setDtPrevistaEntrega(LocalDate dtPrevistaEntrega) {
        this.dtPrevistaEntrega = dtPrevistaEntrega;
    }

    public ZonedDateTime getDtPrevistaEntregaZoned() {
        return dtPrevistaEntregaZoned;
    }

    public void setDtPrevistaEntregaZoned(ZonedDateTime dtPrevistaEntregaZoned) {
        this.dtPrevistaEntregaZoned = dtPrevistaEntregaZoned;
    }

    public Set<Produto> getProdutopeds() {
        return produtopeds;
    }

    public void setProdutopeds(Set<Produto> produtos) {
        this.produtopeds = produtos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PedTeste pedTeste = (PedTeste) o;

        if ( ! Objects.equals(id, pedTeste.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PedTeste{" +
            "id=" + id +
            ", dtPrevistaEntrega='" + dtPrevistaEntrega + "'" +
            ", dtPrevistaEntregaZoned='" + dtPrevistaEntregaZoned + "'" +
            '}';
    }
}
