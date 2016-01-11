package br.com.rexapps.controles.service;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rexapps.controles.domain.Estoque;
import br.com.rexapps.controles.domain.Produto;
import br.com.rexapps.controles.domain.ProdutosPedidos;
import br.com.rexapps.controles.repository.EstoqueRepository;
import br.com.rexapps.controles.repository.PedidoProdutoRepository;
import br.com.rexapps.controles.repository.ProdutoRepository;

@Service
@Transactional
public class ProdutoService {

    private final Logger log = LoggerFactory.getLogger(ProdutoService.class);
    
    @Inject
    private EstoqueRepository estoqueRepository;
    
    @Inject
    private EstoqueService estoqueServices;
    
    @Inject
    private ProdutoRepository produtoRepository;
    
    @Inject
    private PedidoProdutoRepository produtoPedidosRepository;
    
	
	public void removerProduto(Long id) {
		
		Set<ProdutosPedidos> produtosPedidos = produtoPedidosRepository.findByIdProduto(id);
		List<Estoque> listaEstoque = estoqueRepository.findByProduto(id);
		
		if(produtosPedidos.size() <= 0 && listaEstoque != null) {
			estoqueRepository.delete(listaEstoque);
			produtoRepository.delete(id);
		}else{
			Produto produto = produtoRepository.findOne(id);
			produto.setRemovidoLogicamente(true);
			estoqueServices.removerProduto(produto);
			produtoRepository.saveAndFlush(produto);
		}
	}

}
