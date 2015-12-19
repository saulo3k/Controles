package br.com.rexapps.controles.web.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/**
 * A DTO representing a user, with his authorities.
 */
public class QuantidadeDTO {
        
    	@NotNull
    @Size(min = 1, max = 50)
    private long quantidadeCliente;
	
	@NotNull
    @Size(min = 1, max = 50)
    private long quantidadeSeparacao;
	
	@NotNull
    @Size(min = 1, max = 50)
    private long quantidadeEntrega;
	
	@NotNull
    @Size(min = 1, max = 50)
    private long quantidadeEstoque;
	
	@NotNull
    @Size(min = 1, max = 50)
    private long quantidadeUsuario;
	
	@NotNull
    @Size(min = 1, max = 50)
    private long quantidadeProduto;
	
	@NotNull
    @Size(min = 1, max = 50)
    private long quantidadePedido;
	
	@NotNull
    @Size(min = 1, max = 50)
    private long quantidadeCategoria;

    public QuantidadeDTO() {
    }

    public long getQuantidadeCliente() {
		return quantidadeCliente;
	}

	public void setQuantidadeCliente(long quantidadeCliente) {
		this.quantidadeCliente = quantidadeCliente;
	}

	public long getQuantidadeSeparacao() {
		return quantidadeSeparacao;
	}

	public void setQuantidadeSeparacao(long quantidadeSeparacao) {
		this.quantidadeSeparacao = quantidadeSeparacao;
	}

	public long getQuantidadeEntrega() {
		return quantidadeEntrega;
	}

	public void setQuantidadeEntrega(long quantidadeEntrega) {
		this.quantidadeEntrega = quantidadeEntrega;
	}

	public long getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(long quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public long getQuantidadeUsuario() {
		return quantidadeUsuario;
	}

	public void setQuantidadeUsuario(long quantidadeUsuario) {
		this.quantidadeUsuario = quantidadeUsuario;
	}

	public long getQuantidadeProduto() {
		return quantidadeProduto;
	}

	public void setQuantidadeProduto(long quantidadeProduto) {
		this.quantidadeProduto = quantidadeProduto;
	}

	public long getQuantidadePedido() {
		return quantidadePedido;
	}

	public void setQuantidadePedido(long quantidadePedido) {
		this.quantidadePedido = quantidadePedido;
	}

	public long getQuantidadeCategoria() {
		return quantidadeCategoria;
	}

	public void setQuantidadeCategoria(long quantidadeCategoria) {
		this.quantidadeCategoria = quantidadeCategoria;
	}


}
