package br.com.rexapps.controles.service;

import java.time.LocalDate;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.rexapps.controles.domain.Estoque;
import br.com.rexapps.controles.domain.Produto;
import br.com.rexapps.controles.domain.enumeration.OperacaoEstoque;
import br.com.rexapps.controles.repository.EstoqueRepository;
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

	public void ajustarEstoque(Produto produto) {

	}

}
