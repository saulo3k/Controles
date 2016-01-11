--Insere clientes
insert into cliente (cep, cidade, cpf_cnpj, email, endereco, estado, informacoes_para_busca, nome, nome_contato, telefone, tipo_telefone) 
values ('889900-330', 'Rio de Janeiro', '404.404.0001/222', 'carrefour@caferrefource.com', 'rua das amoras', 'RJ', 'carrefour supermercado jordao teste', 'Carrefour', 'Jordao', '011 35672 1100', 'celular')
insert into cliente (cep, cidade, cpf_cnpj, email, endereco, estado, informacoes_para_busca, nome, nome_contato, telefone, tipo_telefone) 
values ('889900-330', 'Rio de Janeiro', '404.404.0001/222', 'carrefour@caferrefource.com', 'rua das amoras', 'RJ', 'carrefour supermercado jordao teste', 'Big', 'Jordao', '011 35672 1100', 'celular');
insert into cliente (cep, cidade, cpf_cnpj, email, endereco, estado, informacoes_para_busca, nome, nome_contato, telefone, tipo_telefone) 
values ('889900-330', 'Rio de Janeiro', '404.404.0001/222', 'carrefour@caferrefource.com', 'rua das amoras', 'RJ', 'carrefour supermercado jordao teste', 'Extra', 'Jordao', '011 35672 1100', 'celular');
insert into cliente (cep, cidade, cpf_cnpj, email, endereco, estado, informacoes_para_busca, nome, nome_contato, telefone, tipo_telefone) 
values ('889900-330', 'Rio de Janeiro', '404.404.0001/222', 'carrefour@caferrefource.com', 'rua das amoras', 'RJ', 'carrefour supermercado jordao teste', 'Pao de Acucar', 'Jordao', '011 35672 1100', 'celular');

--Insere categorias
insert into categoria_produto (ativa, descricao, nome) values (true, 'Verduras', 'Verduras');
insert into categoria_produto (ativa, descricao, nome) values (true, 'Legumes', 'Legumes');
insert into categoria_produto (ativa, descricao, nome) values (true, 'fruta', 'Frutas');
insert into categoria_produto (ativa, descricao, nome) values (true, 'Hortalicias', 'Hortalicias');

--Insere produtos
[Produto{id=null, nome='Cenoura', descricao='cenoura azul', referencia='ref01', codigoBarras='5890385908880', precoCusto='10', precoVenda='12', estoque='2334', vendaSemEstoque='null', promocao='null', dataCadastro='null', unidadeMedida='UN'}]
[DEBUG] br.com.rexapps.controles.web.rest.ProdutoResource - REST request to save Produto : Produto{id=null, nome='Cenoura', descricao='cenoura azul', referencia='ref01', codigoBarras='5890385908880', precoCusto='10', precoVenda='12', estoque='2334', vendaSemEstoque='null', promocao='null', dataCadastro='null', unidadeMedida='UN'}
Hibernate: select nextval ('hibernate_sequence')
insert into produto (categoria_produto_id, codigo_barras, data_cadastro, descricao, estoque, nome, preco_custo, preco_venda, promocao, 
	referencia, unidade_medida, user_id, venda_sem_estoque, id) values 
(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)








Pedencias
1) Tirar validacao do produto preco custo
2) Colocar regra no main para fazer count
3) Aumentar tamanho do botao fechar do modal


[
	{
	"id":1015,
	"nome":"Cenoura",
	"descricao":"cenoura azul",
	"referencia":"ref01",
	"codigoBarras":5890385908880,
	"precoCusto":10,
	"precoVenda":12,
	"estoque":2334,
	"vendaSemEstoque":null,
	"promocao":null,"dataCadastro":"2015-11-17","unidadeMedida":"UN","categoriaProduto":{"id":1,"nome":"Legumes","descricao":"Legumes","ativa":true},"user":null}
]



        $scope.pedido.produtosPedidos = [];
        
        $scope.toggle = function (idx, item, quantidade) {
            var pos = $scope.pedido.produtosPedidos.indexOf(idx);
            if (pos == -1) {
            	$scope.pedido.produtoPedido = { produto:item, quantidade: quantidade, pedido : ""};
            	$scope.pedido.produtosPedidos.push($scope.pedido.produtoPedido);
            	$scope.selection.push(idx); 
            } else {
            	$scope.pedido.produtosPedidos.splice(pos, 1);
            }
        };



org.hibernate.TransientObjectException: object references an unsaved transient instance - save the transient instance before flushing: br.com.rexapps.controles.domain.ProdutosPedidos; nested exception is java.lang.IllegalStateException: org.hibernate.TransientObjectException: object references an unsaved transient instance - save the transient instance before flushing: br.com.rexapps.controles.domain.ProdutosPedidos



ould not read document: Can not deserialize instance of br.com.rexapps.controles.domain.ProdutosPedidos out of START_ARRAY token
 at [Source: java.io.PushbackInputStream@54aef1ad; line: 1, column: 870] (through reference chain: br.com.rexapps.controles.domain.Pedido["produtosPedidos"]->java.util.HashSet[2]); nested exception is com.fasterxml.jackson.databind.JsonMappingException: Can not deserialize instance of br.com.rexapps.controles.domain.ProdutosPedidos out of START_ARRAY token
 at [Source: java.io.PushbackInputStream@54aef1ad; line: 1, column: 870] (through reference chain: br.com.rexapps.controles.domain.Pedido["produtosPedidos"]->java.util.HashSet[2])


 
 
 org.hibernate.TransientObjectException: object references an unsaved transient instance - 
 save the transient instance before flushing: br.com.rexapps.controles.domain.ProdutosPedidos; 
 nested exception is java.lang.IllegalStateException: org.hibernate.TransientObjectException: 
 object references an unsaved transient instance - 
 save the transient instance before flushing: br.com.rexapps.controles.domain.ProdutosPedidos