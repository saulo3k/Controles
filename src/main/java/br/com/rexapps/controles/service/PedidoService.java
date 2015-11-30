package br.com.rexapps.controles.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rexapps.controles.domain.DiaSemana;
import br.com.rexapps.controles.domain.Pedido;
import br.com.rexapps.controles.domain.ProdutosPedidos;
import br.com.rexapps.controles.domain.enumeration.StatusPedido;
import br.com.rexapps.controles.repository.PedidoProdutoRepository;
import br.com.rexapps.controles.repository.PedidoRepository;
import br.com.rexapps.controles.repository.ProdutoRepository;
import br.com.rexapps.controles.repository.UserRepository;
import br.com.rexapps.controles.security.SecurityUtils;

@Service
@Transactional
public class PedidoService {

	private final Logger log = LoggerFactory.getLogger(PedidoService.class);

	@Inject
	private PedidoRepository pedidoRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private ProdutoRepository produtoRepository;

	@Inject
	private PedidoProdutoRepository pedidoProdutoRepository;

	public Pedido savePedido(Pedido pedido) {
		log.debug("Activating user for activation key {}", pedido);

		// Caso a data vier vazia deve colocar a data atual
		if (pedido.getDtPrevistaSeparacao() == null) {
			pedido.setDtPrevistaSeparacao(LocalDate.now());
		}

		if (pedido.getDtPrevistaEntrega() == null) {
			pedido.setDtPrevistaEntrega(LocalDate.now());
		}

		if (pedido.isPedidoModelo()) {
			pedido.setStatusPedido(StatusPedido.PedidoModelo);
		}

		// Seta usuario que cadastrou o pedido
		pedido.setUser_pedido(userRepository.findOne(SecurityUtils.getCurrentUserId()));

		Set<ProdutosPedidos> produtosPedidos = new HashSet<>();
		for (ProdutosPedidos prodPedidoFor : pedido.getProdutosPedidos()) {
			ProdutosPedidos prodPedidoSave = new ProdutosPedidos();
			prodPedidoSave.setPedido(pedido);
			prodPedidoSave.setProduto(produtoRepository.findOne((prodPedidoFor.getProduto().getId())));
			prodPedidoSave.setQuantidade(prodPedidoFor.getQuantidade());
			produtosPedidos.add(prodPedidoSave);
		}
		pedido.setProdutosPedidos(produtosPedidos);

		pedidoRepository.save(pedido);

		return pedido;
	}

	public Pedido enviarPedidoSeparacao(Pedido pedido) {
		log.debug("Activating user for activation key {}", pedido);
		return null;
	}

	public Pedido savePedidoModelo(Pedido pedido) {

		pedido.setStatusPedido(StatusPedido.PedidoModelo);
		pedido.setPedidoModelo(true);

		// Seta usuario que cadastrou o pedido
		pedido.setUser_pedido(userRepository.findOne(SecurityUtils.getCurrentUserId()));

		Set<ProdutosPedidos> produtosPedidos = new HashSet<>();
		for (ProdutosPedidos prodPedidoFor : pedido.getProdutosPedidos()) {
			ProdutosPedidos prodPedidoSave = new ProdutosPedidos();
			prodPedidoSave.setPedido(pedido);
			prodPedidoSave.setProduto(produtoRepository.findOne((prodPedidoFor.getProduto().getId())));
			prodPedidoSave.setQuantidade(prodPedidoFor.getQuantidade());
			produtosPedidos.add(prodPedidoSave);
		}
		pedido.setProdutosPedidos(produtosPedidos);

		pedidoRepository.save(pedido);

		LocalDate dataAtual = LocalDate.now();

		if ((pedido.getPeriodoPedidoInicio().isEqual(dataAtual) || pedido.getPeriodoPedidoFim().isEqual(dataAtual))
				|| (pedido.getPeriodoPedidoInicio().isBefore(dataAtual)
						&& pedido.getPeriodoPedidoFim().isAfter(dataAtual))) {
			boolean criar = false;
			// verifica se pedido esta na lista dos dias
			for (DiaSemana diaSemana : pedido.getDiasSemana()) {
				if (diaSemana.getId() == dataAtual.getDayOfWeek().getValue()) {
					criar = true;
				}
			}
			if (criar) {
				// Cria novo pedido
				gerarPedidoAutomatico(pedido);
			}

		}

		return pedido;
	}

	public Pedido updatePedido(Pedido pedido) {

		Set<ProdutosPedidos> produtosPedidosSeremRemovidos = new HashSet<>();

		produtosPedidosSeremRemovidos = pedidoProdutoRepository.findByIdPedidos(pedido.getId());
		for (ProdutosPedidos produtosremover : produtosPedidosSeremRemovidos) {
			pedidoProdutoRepository.delete(produtosremover.getId());
			pedidoProdutoRepository.flush();
		}

		Set<ProdutosPedidos> produtosPedidos = new HashSet<>();
		for (ProdutosPedidos prodPedidoFor : pedido.getProdutosPedidos()) {
			ProdutosPedidos prodPedidoSave = new ProdutosPedidos();
			prodPedidoSave.setPedido(pedido);
			prodPedidoSave.setProduto(produtoRepository.findOne((prodPedidoFor.getProduto().getId())));
			prodPedidoSave.setQuantidade(prodPedidoFor.getQuantidade());
			produtosPedidos.add(prodPedidoSave);
		}

		pedido.setProdutosPedidos(produtosPedidos);
		pedidoRepository.save(pedido);
		return pedido;
	}

	public void gerarPedidoAutomatico(Pedido pedidoParam) {
		Pedido pedido = new Pedido();
		pedido.setStatusPedido(StatusPedido.PedidoGeradoAutomaticamente);
		pedido.setPedidoModelo(false);
		pedido.setDtPrevistaSeparacao(LocalDate.now());
		pedido.setDtPrevistaEntrega(LocalDate.now());
		pedido.setUser_pedido(userRepository.findOne(pedidoParam.getUser_pedido().getId()));

		Set<ProdutosPedidos> produtosPedidos = new HashSet<>();
		for (ProdutosPedidos prodPedidoFor : pedidoParam.getProdutosPedidos()) {
			ProdutosPedidos prodPedidoSave = new ProdutosPedidos();
			prodPedidoSave.setPedido(pedido);
			prodPedidoSave.setProduto(produtoRepository.findOne((prodPedidoFor.getProduto().getId())));
			prodPedidoSave.setQuantidade(prodPedidoFor.getQuantidade());
			produtosPedidos.add(prodPedidoSave);
		}
		pedido.setCliente_pedido(pedidoParam.getCliente_pedido());
		pedido.setDataPedido(LocalDate.now());
		pedido.setProdutosPedidos(produtosPedidos);
		pedidoRepository.save(pedido);
	}

	// @Scheduled(fixedRate = 5000)
	public void agendadorVerificarGeracaoPedidos() {
		System.out.println("pijama");
	}
}
