'use strict';

angular.module('controlesApp').controller('PedidoSeparacaoListDialogController',
    ['$scope', '$stateParams', '$modalInstance',  'entity', 'Pedido', 'Produto', 'User', 'Cliente','PedidoSeparacao', 'PedidoEqualizar', 'ClienteProduto', '$window',
        function($scope, $stateParams, $modalInstance, entity, Pedido, Produto, User, Cliente, PedidoSeparacao, PedidoEqualizar, ClienteProduto, $window) {
    	$scope.pedidos = [];
    	$scope.Produtospedidos = []; 
        $scope.page = 0;
        $scope.produtos = Produto.query({page: $scope.page, size: 7000});
        $scope.users = User.query();
        $scope.clientes = Cliente.query({page: $scope.page, size: 9000});
        
        $scope.selection = [];   
		$scope.separados = [];
        
		$scope.loadAll = function() {
        	PedidoEqualizar.query({page: $scope.page, size: 500}, function(result, headers) {
        		console.log("passou aqui");
                for (var i = 0; i < result.length; i++) {
                    
                	$scope.pedidos.push(result[i]);
                    var separar = [];
                	var pedido = result[i];
                	
                	$scope.buscarPrecosExclusivos(pedido.cliente_pedido.id);
                	
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
        
        $scope.produtoPrecoExclusivo = {};
        
        $scope.buscarPrecosExclusivos = function(id){	
        	console.log("oi",id);
        	ClienteProduto.get({id : id}, function(result, headers) {
                for (var i = 0; i < result.length; i++) {                
                	var clienteProd = result[i];
                	$scope.clienteProdutos.push(clienteProd);               
                	
                	for (var j = 0; j < $scope.produtos.length; j++) {
            			var valueProduto = $scope.produtos[j];
        				if(valueProduto.id == clienteProd.produto.id){
    						valueProduto.precoVenda = clienteProd.precoVenda;		    						
    					}            				
                	}	                	
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
        	        	        	
        	pedido.total = 0;
        	        	
        	for(var i=0; i < pedido.produtosPedidos.length; i++) {
        		
        		var produtoPedido = pedido.produtosPedidos[i];
        		
        		var precoVenda = Number(produtoPedido.produto.precoVenda || 0);
        		
            	for (var j = 0; j < $scope.produtos.length; j++) {
        			var valueProduto = $scope.produtos[j];
    				if(valueProduto.id == produtoPedido.produto.id){
    					precoVenda = valueProduto.precoVenda;
    					if(produtoPedido.quantidadeNew != null){
    	        	 		pedido.total += produtoPedido.quantidadeNew * precoVenda;  
    	        	 	}else{
    	        	 		pedido.total += produtoPedido.quantidade * precoVenda;
    	        	 	}
					}            				
            	}
        		    	    			        	 	
        		        		
//                for(var g = 0; g < $scope.clienteProdutos.length; g++) {
//                	var clienteProd = $scope.clienteProdutos[g];
//                	if(pedido.cliente_pedido.id == clienteProd.cliente.id) {
//                    	console.log("clienteProd", clienteProd.cliente.id);
//                    	
//                    	console.log("produtoPedido.produto.id",produtoPedido.produto.nome);
//	                	if(produtoPedido.produto.id == clienteProd.produto.id) {
//	                		
//
////	    	    			break;
//	    	    		}	
//                	}
//                } 
        		
        		if(produtoPedidoParam.produto.id == produtoPedido.produto.id) {        			        			        			        	
            		
            		for(var h=0; h < $scope.separados.length; h++) {
            			var separado = $scope.separados[h];
            			
            			if(separado.id == produtoPedidoParam.produto.id) {            						
    						var oldQuantidade = new Number(produtoPedidoParam.quantidade);
    						var newQuantidade = new Number(produtoPedidoParam.quantidadeNew);            						   						
    						var estoque = separado.estoque + oldQuantidade;
    						separado.estoque = estoque - newQuantidade;
                			break;		
            			}            			
            		}        
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
             

        var onSaveFinished = function (result) {
            $scope.$emit('controlesApp:pedidoUpdate', result);
        };
        
        

        $scope.IsHidden = false;
        $scope.ShowHide = function () {            
            $scope.IsHidden = $scope.IsHidden ? false : true;
        }

        $scope.save = function (pedido) {
            if (pedido.id != null) {
            	pedido.statusPedido = 'EmSeparacao';
            	for(var i=0; i < pedido.produtosPedidos.length; i++) {
            		if(pedido.produtosPedidos[i].quantidadeNew != null){
            			console.log("entrou aqui");
            			pedido.produtosPedidos[i].quantidade = pedido.produtosPedidos[i].quantidadeNew;	
            		}            		 
            	}
                Pedido.update(pedido, onSaveFinished);
                var pos = $scope.pedidos.indexOf(pedido); 
                console.log(pos);
                var remover = $scope.pedidos.splice(pos, 1);
                if($scope.pedidos.length == 0){                
                	$scope.clear(); 
                }
            }
        };

        $scope.clear = function() {    		
            $modalInstance.dismiss('cancel');            
            $window.location.href = '#/pedidos';
            Pedido.query();
        };                                        
}]);
