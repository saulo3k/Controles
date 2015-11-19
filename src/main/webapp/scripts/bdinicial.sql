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
