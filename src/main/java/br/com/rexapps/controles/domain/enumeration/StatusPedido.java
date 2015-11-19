package br.com.rexapps.controles.domain.enumeration;

/**
 * The StatusPedido enumeration.
 */
public enum StatusPedido {
	PedidoModelo(1),
	ClienteNaoPediu(2),
	EmProcessoPedido(3),
	Separacao(4),
	EmSeparacao(5),
	Romaneio(6);
	
	int codigo;
	StatusPedido(int p) {
	    codigo = p;
	}
	int showCodigo() {
	      return codigo;
	} 
}
