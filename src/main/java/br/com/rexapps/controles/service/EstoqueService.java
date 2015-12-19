package br.com.rexapps.controles.service;

import java.time.LocalDate;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rexapps.controles.domain.Estoque;
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

	public void removerProdutoEstoque(Produto produto) {

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
        	estoque.setQuantidadeAtual(quantidadeAtual);
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
