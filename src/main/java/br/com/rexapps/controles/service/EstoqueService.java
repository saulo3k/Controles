package br.com.rexapps.controles.service;

import java.time.LocalDate;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rexapps.controles.domain.Estoque;
import br.com.rexapps.controles.domain.Pedido;
import br.com.rexapps.controles.domain.Produto;
import br.com.rexapps.controles.domain.ProdutosPedidos;
import br.com.rexapps.controles.domain.User;
import br.com.rexapps.controles.domain.enumeration.OperacaoEstoque;
import br.com.rexapps.controles.repository.EstoqueRepository;
import br.com.rexapps.controles.repository.ProdutoRepository;
import br.com.rexapps.controles.repository.UserRepository;
import br.com.rexapps.controles.security.SecurityUtils;

@Service
@Transactional
public class EstoqueService {

    private final Logger log = LoggerFactory.getLogger(EstoqueService.class);
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private EstoqueRepository estoqueRepository;
    
    @Inject
    private ProdutoRepository produtoRepository;
    
	public void adicionarProdutoEstoque(Produto produto) {

		Estoque estoqueAdicao = new Estoque();
		estoqueAdicao.setDataAtual(LocalDate.now());
		estoqueAdicao.setEstoque_produto(produto);
		estoqueAdicao.setEstoque_user(userRepository.findOne(SecurityUtils.getCurrentUserId()));
		estoqueAdicao.setMotivo("Cadastro de Produto");
		estoqueAdicao.setOperacao(OperacaoEstoque.Entrada);
		estoqueAdicao.setQuantidadeAtual(0L);
		estoqueAdicao.setQuantidadeAposMovimentacao(produto.getEstoque());
		estoqueRepository.save(estoqueAdicao);
	}
	
	public void devolverProdutoEstoque(Produto produto) {

		Estoque estoqueAdicao = new Estoque();
		estoqueAdicao.setDataAtual(LocalDate.now());
		estoqueAdicao.setEstoque_produto(produto);
		estoqueAdicao.setEstoque_user(userRepository.findOne(SecurityUtils.getCurrentUserId()));
		estoqueAdicao.setMotivo("Devolução de produto ao estoque devido a edicao de pedidos");
		estoqueAdicao.setOperacao(OperacaoEstoque.Entrada);
		estoqueAdicao.setQuantidadeAtual(produto.getEstoque());
		estoqueAdicao.setQuantidadeAposMovimentacao(produto.getEstoque());
		estoqueRepository.save(estoqueAdicao);
	}
	
	public void devolverProdutoEstoque(Produto produto, Long quantidade, Pedido pedido) {

		Estoque estoqueAdicao = new Estoque();
		estoqueAdicao.setDataAtual(LocalDate.now());
		estoqueAdicao.setEstoque_produto(produto);
		estoqueAdicao.setEstoque_user(userRepository.findOne(SecurityUtils.getCurrentUserId()));
		estoqueAdicao.setMotivo("Remoção do pedido:" + pedido.getId() + "Que se encontrava em:" + pedido.getStatusPedido().name());
		estoqueAdicao.setOperacao(OperacaoEstoque.Saida);
		estoqueAdicao.setQuantidadeAtual(produto.getEstoque());
		Long total = produto.getEstoque() + quantidade;
		estoqueAdicao.setQuantidadeAposMovimentacao(total);
		
		estoqueRepository.save(estoqueAdicao);
		
		produto.setEstoque(total);
		
		produtoRepository.save(produto);
	}

	public void removerProduto(Produto produto) {
		Estoque estoque = new Estoque();
		
		estoque.setOperacao(OperacaoEstoque.Saida);
		estoque.setDataAtual(LocalDate.now());
		estoque.setEstoque_user(userRepository.findOne(SecurityUtils.getCurrentUserId()));
		estoque.setMotivo("O usuário:" + estoque.getEstoque_user().getLogin() + "removeu o produto " + produto.getNome() + " da lista de produtos");
		
		estoque.setQuantidade(produto.getEstoque());
    	estoque.setQuantidadeAtual(produto.getEstoque());
    	estoque.setQuantidadeAposMovimentacao(0L);
		
		estoqueRepository.save(estoque);
		
	}
	
	public void removerProdutosEstoque(Set<ProdutosPedidos> produtos, User user) {
		
		for (ProdutosPedidos produtosPedidos : produtos) {			
			Estoque estoque = new Estoque();
			
			estoque.setOperacao(OperacaoEstoque.Saida);
			estoque.setDataAtual(LocalDate.now());
			estoque.setEstoque_user(user);
			estoque.setMotivo("Saída de Produto por Separação");					
			
			Long quantidadeAtual = produtosPedidos.getProduto().getEstoque();
			
			estoque.setQuantidade(produtosPedidos.getQuantidade());
			if(quantidadeAtual != null){				
				estoque.setQuantidadeAtual(quantidadeAtual);
			}else{
				quantidadeAtual = 0L;
			}
        	estoque.setQuantidadeAposMovimentacao(quantidadeAtual - produtosPedidos.getQuantidade());
        	produtosPedidos.getProduto().setEstoque(quantidadeAtual - produtosPedidos.getQuantidade());
        	estoque.setEstoque_produto(produtosPedidos.getProduto());
        	estoque.getEstoque_produto().setEstoque(quantidadeAtual - produtosPedidos.getQuantidade());        	
        	estoqueRepository.save(estoque);
        	produtoRepository.save(produtosPedidos.getProduto());
        }        					

	}

	public void ajustarEstoque(Produto produto) {

	}

}
