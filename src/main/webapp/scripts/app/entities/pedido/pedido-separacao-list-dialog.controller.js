'use strict';

angular.module('controlesApp').controller('PedidoSeparacaoListDialogController',
    ['$scope', '$stateParams', '$modalInstance',  'entity', 'Pedido', 'Produto', 'User', 'Cliente','PedidoSeparacao', 'PedidoEqualizar', 'ClienteProduto',
        function($scope, $stateParams, $modalInstance, entity, Pedido, Produto, User, Cliente, PedidoSeparacao, PedidoEqualizar, ClienteProduto) {
    	$scope.pedidos = [];
    	$scope.Produtospedidos = []; 
        $scope.page = 0;
        $scope.produtos = Produto.query();
        $scope.users = User.query();
        $scope.clientes = Cliente.query();
        $scope.selection = [];   
		$scope.separados = [];
        
		$scope.loadAll = function() {
        	PedidoEqualizar.query({page: $scope.page, size: 50}, function(result, headers) {
             	
                for (var i = 0; i < result.length; i++) {
                    
                	$scope.pedidos.push(result[i]);
                    var separar = [];
                	var pedido = result[i];
                	                	            		                	                    	                                
                	for (var j = 0; j < $scope.produtos.length; j++) {
                		
                		var valueProduto = $scope.produtos[j]

                		for (var x = 0; x < pedido.produtosPedidos.length; x++) {
                			$scope.Produtospedidos.push(pedido.produtosPedidos[x]);
							var produtoPedido = pedido.produtosPedidos[x];
							var valuePedido = pedido.produtosPedidos[x].produto;
							        						        					
							if(valueProduto.id == valuePedido.id) {		    							      	    							      	    							  
							  
							  valueProduto.estoque = valueProduto.estoque - produtoPedido.quantidade;
				              if($scope.separados.indexOf(valueProduto) == -1) {
				            	  $scope.separados.push(valueProduto);
				            	   
				              }    	    				              
							}
							
    					};		    					
    				};    	        	        	                    
                };               
            });        	
        };
        
        $scope.loadAll();
        
        $scope.clienteProdutos = [];
        
        $scope.buscarPrecosExclusivos = function(id){			
        	ClienteProduto.get({id : id}, function(result, headers) {
                for (var i = 0; i < result.length; i++) {                	                 	
                	$scope.clienteProdutos.push(result[i]);
                	var clienteProd = result[i];
                	
                	var keepGoing = true;
                	
                	$scope.produtos.$promise.then(function(dataProd) {
            			angular.forEach(dataProd, function(valueProduto, indexProduto) {
            				if(keepGoing){
	            				if(valueProduto.id == clienteProd.produto.id){
		    						valueProduto.precoVenda = clienteProd.precoVenda;
		    						console.log('Preco para venda', valueProduto.precoVenda);
		    						keepGoing = false;
		    					}
            				}
            			});
                	});	                	
                }
        	});
        };        
        
        $scope.valoresAntigos = [];       
        $scope.$watch('Produtospedidos',  function(newValue, oldValue) {
        	$scope.valoresAntigos = [];       
        	if(oldValue != 'undefined') {
        		for(var i =0; i < oldValue.length; i++){
                	$scope.valoresAntigos.push(oldValue[i]);	
                }             	
        	}               
        }, true);
                
        
             
        $scope.calculaTotal = function (produtoPedidoParam, pedido) {
        	
        	//Vou precisa do produto que esta sendo alterado. para modificar sua quantidade para hora de salvar
        	//vou precisar dos precos exclusivos deste cliente para verificar se este produto tem preco diferenciado
        	//De todos os produtos para somar o valor
        	//Da separacao para ajustar a quantidade do estoque
        	        	
        	
        	pedido.total = 0;
        	
        	for(var i=0; i < pedido.produtosPedidos.length; i++) {
        		
        		var produtoPedido = pedido.produtosPedidos[i];
        		
        		if(produtoPedidoParam.produto.id == produtoPedido.produto.id) {        			        			
        			        			
        			var precoVenda = Number(produtoPedidoParam.produto.precoVenda || 0);
            		var quantidade = Number(produtoPedidoParam.quantidade || 0);
            		
            		pedido.total = pedido.total + (precoVenda  * quantidade);
            		
            		for(var h=0; h < $scope.separados.length; h++) {
            			var separado = $scope.separados[h];
            			
            			if(separado.id == produtoPedidoParam.produto.id) {
            				console.log(separado.nome);
            				console.log(pedido.id);
            				for(var p = 0; p < $scope.valoresAntigos.length; p++){
            					if($scope.valoresAntigos[p].id == produtoPedido.id) {            						
            						var oldQuantidade = $scope.valoresAntigos[p].quantidade;
            						var newQuantidade = produtoPedido.quantidade;            						
            						console.log('new', newQuantidade);
            						console.log('old', oldQuantidade);
                    				if(newQuantidade > oldQuantidade) {
//                						soma
                    					separado.estoque = oldQuantidade - separado.estoque;
                    					separado.estoque = newQuantidade + separado.estoque;
                    				}
                					if(newQuantidade < oldQuantidade) {
//                						subtrai
                						var estoque = oldQuantidade - separado.estoque;                						
                						separado.estoque = newQuantidade - estoque;
                					}
            						break;
            					}
            				}
            			}            			
            		}

        		}else {
        			var precoVenda = Number(produtoPedido.produto.precoVenda || 0);
            		var quantidade = Number(produtoPedido.quantidade || 0);
            		pedido.total = pedido.total + (precoVenda  * quantidade);	
        		}
        		
        	}
                   	
        }                           
                                      
        $scope.load = function(id) {
            Pedido.get({id : id}, function(result) {            	
                $scope.pedido = result;
            });                         
        };                       
        $scope.separados = [];
        
        $scope.produtosSelecionadosPedidos = [];        
                
        $scope.toggle = function (idx, produto) {
            var pos = $scope.selection.indexOf(idx);
            if (pos == -1) {
                $scope.selection.push(idx);                
                $scope.produtosSelecionadosPedidos.push(produto);
            } else {
                $scope.selection.splice(pos, 1);
                var remover = $scope.produtosSelecionadosPedidos.splice(pos, 1);
                for (var i=0; i < $scope.pedido.produtosPedidos.length;i++){
                	
                	if(remover[0].id == $scope.pedido.produtosPedidos[i].produto.id){                		
                		 $scope.pedido.produtosPedidos.splice(i, 1);
                	}	
                };
            }
        };
             

        var onSaveFinished = function (result) {
            $scope.$emit('controlesApp:pedidoUpdate', result);
            $modalInstance.close(result);
        };
        
        $scope.saveParam = function (acao) {
        	if(acao == 0){
        		$scope.status = 'EmProcessoPedido';	
        	}else if(acao == 1){
        		$scope.status = 'EmSeparacao';
        	}
        };

        $scope.IsHidden = false;
        $scope.ShowHide = function () {            
            $scope.IsHidden = $scope.IsHidden ? false : true;
        }

        $scope.save = function () {
        	console.log($scope.status);
        	$scope.pedido.statusPedido = $scope.status;
            if ($scope.pedido.id != null) {            	
                Pedido.update($scope.pedido, onSaveFinished);
            } else {            	
                Pedido.save($scope.pedido, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };                                        
}]);
