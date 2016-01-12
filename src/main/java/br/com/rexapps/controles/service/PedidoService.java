package br.com.rexapps.controles.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rexapps.controles.domain.ClienteProduto;
import br.com.rexapps.controles.domain.DiaSemana;
import br.com.rexapps.controles.domain.Pedido;
import br.com.rexapps.controles.domain.ProdutosPedidos;
import br.com.rexapps.controles.domain.enumeration.StatusPedido;
import br.com.rexapps.controles.repository.ClienteProdutoRepository;
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
	private EstoqueService estoqueService;

	@Inject
	private ProdutoRepository produtoRepository;

	@Inject
	private PedidoProdutoRepository pedidoProdutoRepository;
	
	@Inject
	private ClienteProdutoRepository clienteProdutoRepository;

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
		
		if(pedido.getStatusPedido().equals(StatusPedido.EmSeparacao) && !pedido.isPedidoModelo()) {
			estoqueService.removerProdutosEstoque(pedido.getProdutosPedidos(), pedido.getUser_pedido_separacao());
		}

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
				if (diaSemana.getId() == (dataAtual.getDayOfWeek().getValue() + 1)) {
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
		
		Pedido pedidoAntigo = pedidoRepository.getOne(pedido.getId());
		
		Set<ProdutosPedidos> produtosPedidosSeremRemovidos = new HashSet<>();
		
		if(pedido.getStatusPedido().equals(StatusPedido.EmProcessoPedido) && 
		   (pedidoAntigo.getStatusPedido().equals(StatusPedido.Separacao) || pedidoAntigo.getStatusPedido().equals(StatusPedido.EmSeparacao) || pedidoAntigo.getStatusPedido().equals(StatusPedido.Romaneio))){
			//Devolver estoque
			for (ProdutosPedidos produtosPedidos : pedidoAntigo.getProdutosPedidos()) {			
				estoqueService.devolverProdutoEstoque(produtosPedidos.getProduto(), produtosPedidos.getQuantidade(), pedidoAntigo);
			}
		}
		
		for (ProdutosPedidos produtosremover : pedidoAntigo.getProdutosPedidos()) {
			pedidoProdutoRepository.delete(produtosremover.getId());
			pedidoAntigo.setProdutosPedidos(null);
			pedidoRepository.save(pedidoAntigo);
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
		
		if(pedido.getStatusPedido().equals(StatusPedido.EmSeparacao) && !pedido.isPedidoModelo()) {
			estoqueService.removerProdutosEstoque(pedido.getProdutosPedidos(), pedido.getUser_pedido_separacao());
		}
		pedidoRepository.save(pedido);
		pedidoRepository.flush();
		return pedido;
	}
	
	public Pedido separarPedido(Pedido pedido) {
		if(pedido.getStatusPedido() == StatusPedido.EmSeparacao){
			pedido.setUser_pedido_separacao(userRepository.findOne(SecurityUtils.getCurrentUserId()));
		}else{
			pedido.setUser_pedido_separacao(userRepository.findOne(SecurityUtils.getCurrentUserId()));
			pedido.setStatusPedido(StatusPedido.Romaneio);
			pedido.setDtRealSeparacao(LocalDate.now());				
		}		
	    pedidoRepository.save(pedido);
		return pedido;
	}
	
	public Pedido entregarPedido(Pedido pedido) {
		pedido.setUser_pedido_entrega(userRepository.findOne(SecurityUtils.getCurrentUserId()));
		pedido.setStatusPedido(StatusPedido.SaiuParaRomaneio);
		pedido.setDtRealEntrega(LocalDate.now());
	    pedidoRepository.save(pedido);
		return pedido;
	}

	public Pedido gerarPedidoAutomatico(Pedido pedidoParam) {
		Pedido pedido = new Pedido();
		pedido.setStatusPedido(StatusPedido.PedidoGeradoAutomaticamente);
		pedido.setPedidoModelo(false);
		pedido.setDtPrevistaSeparacao(LocalDate.now());
		pedido.setDtPrevistaEntrega(LocalDate.now());
		pedido.setUser_pedido(userRepository.findOne(pedidoParam.getUser_pedido().getId()));

		//Busca precos exclusivos para pagamento
		List<ClienteProduto> precosExclusivos = clienteProdutoRepository.findAllbyCliente(pedido.getCliente_pedido().getId());
		
		Set<ProdutosPedidos> produtosPedidos = new HashSet<>();
		
		for (ProdutosPedidos prodPedidoFor : pedidoParam.getProdutosPedidos()) {
			ProdutosPedidos prodPedidoSave = new ProdutosPedidos();
			prodPedidoSave.setPedido(pedido);
			prodPedidoSave.setProduto(produtoRepository.findOne((prodPedidoFor.getProduto().getId())));
			prodPedidoSave.setQuantidade(prodPedidoFor.getQuantidade());
			
			for (ClienteProduto produto : precosExclusivos) {
				if (produto.getId() == prodPedidoSave.getProduto().getId()) {
					prodPedidoSave.getProduto().setPrecoVenda(produto.getPrecoVenda());
					break;
				}
			}  
			
			produtosPedidos.add(prodPedidoSave);
		}
		pedido.setCliente_pedido(pedidoParam.getCliente_pedido());
		pedido.setDataPedido(LocalDate.now());
		pedido.setProdutosPedidos(produtosPedidos);
		
		pedidoRepository.save(pedido);
		return pedido;
	}

	@Scheduled(cron="0 0 0 * * ?")
	public void agendadorVerificarGeracaoPedidos() {
		
		List<Pedido> pedidos = pedidoRepository.findAllPedidosModelosAutomaticos();
		
		LocalDate dataAtual = LocalDate.now();
		
		for (Pedido pedido : pedidos) {
				
			if ((pedido.getPeriodoPedidoInicio().isEqual(dataAtual) || pedido.getPeriodoPedidoFim().isEqual(dataAtual))
					|| (pedido.getPeriodoPedidoInicio().isBefore(dataAtual)
							&& pedido.getPeriodoPedidoFim().isAfter(dataAtual))) {
				boolean criar = false;
				// verifica se pedido esta na lista dos dias
				for (DiaSemana diaSemana : pedido.getDiasSemana()) {
					if (diaSemana.getId() == (dataAtual.getDayOfWeek().getValue() + 1)) {
						criar = true;
					}
				}
				if (criar) {
					// Cria novo pedido
					gerarPedidoAutomatico(pedido);
				}
	
			}
		}
	}

	public EstoqueService getEstoqueService() {
		return estoqueService;
	}

	public void setEstoqueService(EstoqueService estoqueService) {
		this.estoqueService = estoqueService;
	}
	
	public void remover(Long id) {
		//devolver estoque
		Pedido pedido = pedidoRepository.findOneWithEagerRelationshipsDaysOfWeek(id);
		if (pedido.getStatusPedido().equals(StatusPedido.EmSeparacao) || 
				pedido.getStatusPedido().equals(StatusPedido.Romaneio) || 
				pedido.getStatusPedido().equals(StatusPedido.SaiuParaRomaneio) ||  
				pedido.getStatusPedido().equals(StatusPedido.Separacao)) {
			for (ProdutosPedidos produtosPedidos : pedido.getProdutosPedidos()) {			
				estoqueService.devolverProdutoEstoque(produtosPedidos.getProduto(), produtosPedidos.getQuantidade(), pedido);
			} 
		}
		pedidoRepository.delete(id);
	}

}
