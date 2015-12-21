'use strict';

angular.module('controlesApp').controller('PedidoSeparacaoListDialogController',
    ['$scope', '$stateParams', '$modalInstance',  'entity', 'Pedido', 'Produto', 'User', 'Cliente','PedidoSeparacao', 'PedidoEqualizar', 'ClienteProduto', '$window',
        function($scope, $stateParams, $modalInstance, entity, Pedido, Produto, User, Cliente, PedidoSeparacao, PedidoEqualizar, ClienteProduto, $window) {
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
        	ClienteProduto.get({id : id}, function(result, headers) {
                for (var i = 0; i < result.length; i++) {                	                 	                	
                	$scope.clienteProdutos.push(result[i]);
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
        		
        		if(produtoPedidoParam.produto.id == produtoPedido.produto.id) {        			        			        			        	
            		
            		for(var h=0; h < $scope.separados.length; h++) {
            			var separado = $scope.separados[h];
            			
            			if(separado.id == produtoPedidoParam.produto.id) {            						
    						var oldQuantidade = new Number(produtoPedidoParam.quantidade);
    						var newQuantidade = new Number(produtoPedidoParam.quantidadeNew);            						   						
    						var estoque = separado.estoque + oldQuantidade;
    						separado.estoque = estoque - newQuantidade;
        					produtoPedidoParam.quantidade = newQuantidade;
                			break;		
            			}            			
            		}        
        		}
        		              
        		var precoVenda = Number(produtoPedido.produto.precoVenda || 0);
                for(var g = 0; g < $scope.clienteProdutos.length; g++) {
                	var clienteProd = $scope.clienteProdutos[g];
                	if(pedido.cliente_pedido.id == clienteProd.cliente.id) {
	                	if(produtoPedido.produto.id == clienteProd.produto.id) {	                		
	    	    			precoVenda = clienteProd.precoVenda;
	    	    			break;
	    	    		}	
                	}
                }                            	                                                                             
    			
        		var quantidade = Number(produtoPedido.quantidade || 0);
        		pedido.total = pedido.total + (precoVenda  * quantidade);	        		        	
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
//            $modalInstance.close(result);
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

        $scope.save = function (pedido) {
            if (pedido.id != null) {
            	pedido.statusPedido = 'EmSeparacao';
                Pedido.update(pedido, onSaveFinished);
                var pos = $scope.pedidos.indexOf(pedido); 
                console.log(pos);
                var remover = $scope.pedidos.splice(pos, 1);
                console.log(remover);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
            console.log('oi', $window);
            $window.location.href = '#/pedidos';
        };                                        
}]);
