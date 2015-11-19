package br.com.rexapps.controles.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.elasticsearch.annotations.Document;

@Entity
@Table(name = "produto_pedidos")
@Document(indexName = "produtoPedidos")
public class ProdutosPedidos {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="produto_id")	
    private Produto produto;
    
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="pedido_id")
    private Pedido pedido;
    
    @Column(name = "quantidade")
    private Long quantidade;
    
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

}
