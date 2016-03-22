'use strict';

angular.module('controlesApp').controller('PedidoSeparacaoListDialogController',
    ['$scope', '$stateParams', '$modalInstance',  'entity', 'Pedido', 'Produto', 'User', 'Cliente','PedidoSeparacao', 'PedidoEqualizar', 'ClienteProduto', '$window', '$timeout','$rootScope',
        function($scope, $stateParams, $modalInstance, entity, Pedido, Produto, User, Cliente, PedidoSeparacao, PedidoEqualizar, ClienteProduto, $window, $timeout, $rootScope) {
    	
    	$scope.pedidos = [];
    	$scope.Produtospedidos = []; 
        $scope.page = 0;
        $scope.produtos = Produto.query({page: $scope.page, size: 7000});
        $scope.users = User.query();
        $scope.clientes = Cliente.query({page: $scope.page, size: 9000});
        
        $scope.selection = [];   
		$scope.separados = [];

		$scope.calcularDesconto = function (pedido) {
			if(typeof pedido.desconto == "undefined"){        		
        		pedido.totalDesconto =  pedido.total;
        	}else{
        		pedido.totalDesconto =  pedido.total - pedido.desconto;
        	}  
        };
		
		$scope.loadAll = function() {
        	PedidoEqualizar.query({page: $scope.page, size: 500}, function(result, headers) {
                for (var i = 0; i < result.length; i++) {
                    
                	$scope.pedidos.push(result[i]);
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
        
        $scope.buscarPrecosExclusivos = function(id) {
        	
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
                
        $scope.controlaEstoque = function (produtoPedidoParam) {
        	
        	var estoque = estoque = produtoPedidoParam.produto.estoque;
        	var firstLoop = true;        	
        	
//        	console.log("produtoPedido.quantidadeNew",produtoPedidoParam.quantidadeNew);
        	
        	for(var i=0; i < $scope.pedidos.length; i++) {
        		
        		var pedido = $scope.pedidos[i];        		        		
        		
        		for(var x=0; x < pedido.produtosPedidos.length; x++) {
        			
        			var produtoPedido = pedido.produtosPedidos[x];
        			
		    		if(produtoPedidoParam.produto.id == produtoPedido.produto.id) {     
		    			
//		    			console.log('Pedido que tem este produto', pedido.id);
		    			
		    			for(var h=0; h < $scope.separados.length; h++) {
		            		
		        			var separado = $scope.separados[h];
		        			
		        			if(separado.id == produtoPedidoParam.produto.id) {
		        				
		        				if(firstLoop){
//		        					console.log("frist", estoque);
		        					separado.estoque = estoque;
		        					firstLoop = false;
		        				}
		    					var oldQuantidade = new Number(produtoPedido.quantidade);
		    					var newQuantidade = new Number(produtoPedido.quantidadeNew);
//		    					console.log('oldQuantidade', oldQuantidade);
//		    					console.log('newQuantidade', newQuantidade);
		    					
		    					
		    					if(isNaN(newQuantidade) || produtoPedidoParam.quantidadeNew == null) {
		    						newQuantidade = oldQuantidade;
		    					}
		    					
		    					if(newQuantidade < 0) {
//		    						console.log('#############', newQuantidade);
		    						separado.estoque = separado.estoque - oldQuantidade;        			        	        			        	
		    						$scope.quantidadeNegativa = true;
		    						produtoPedidoParam.quantidadeNew = null;
		    						produtoPedido.quantidadeNew = null;
		    						$timeout(callAtTimeout, 10000);
//		    						return false;        						
		    					}else {		    						 		
//		    						console.log('#####calc newQuantidade', newQuantidade);
		    						separado.estoque = separado.estoque - newQuantidade;
//		    						console.log('#####calc estoque', separado.estoque);
		    					}
		    					if(separado.estoque <= 0){
		    						$scope.produtoEstoqueMenor = produtoPedido.produto;
		    						$scope.estoqueMenor = true;
		    						$timeout(callAtTimeout, 10000);
		    					}			    					    						    						    						    									            				
			        		}            			
			        	}		     	       
		    		}
        		}
        	}        	 
        }
             
        $scope.calculaTotal = function (produtoPedidoParam, pedido) {
        	if(isNaN(produtoPedidoParam.quantidadeNew) || produtoPedidoParam.quantidadeNew < 0) {
        		return false;
        	}
        	
        	pedido.total = 0;
        	        	
        	for(var i=0; i < pedido.produtosPedidos.length; i++) {
        		
        		var produtoPedido = pedido.produtosPedidos[i];
        		
        		var precoVenda = Number(produtoPedido.produto.precoVenda || 0);
        		        	        		
            	for (var j = 0; j < $scope.produtos.length; j++) {
            			
            		var valueProduto = $scope.produtos[j];
        			
    				if(valueProduto.id == produtoPedido.produto.id){
    					var precoEspecial = false;	
    					for (var x = 0; x < $scope.clienteProdutos.length; x++) {
    						var clienteProduto = $scope.clienteProdutos[x];    						
    						if(clienteProduto.cliente.id == pedido.cliente_pedido.id ){    					
    							if(clienteProduto.produto.id == valueProduto.id){    								    							
    								precoEspecial = true;
    							}
    						}
    					}
    					if(precoEspecial){
    						precoVenda = valueProduto.precoVenda;
    					}			
    					if(produtoPedido.quantidadeNew != null) {
    	        	 		pedido.total += produtoPedido.quantidadeNew * precoVenda;    						
    	        	 	}else{
    	        	 		pedido.total += produtoPedido.quantidade * precoVenda;
    	        	 	}
					}            				
            	}        		    	    			        	 	        		                		        		                                         	                                                                                 	
        	}
        	$scope.calcularDesconto(pedido);
        }
        
        function callAtTimeout() {
        	$scope.produtoEstoqueMenor = null;
			$scope.estoqueMenor = false;
			$scope.quantidadeNegativa = false;
        }
                                      
        $scope.load = function(id) {
            Pedido.get({id : id}, function(result) {            	
                $scope.pedido = result;
            });                         
        };                       
        $scope.separados = [];
        
        $scope.produtosSelecionadosPedidos = [];               
        
        $scope.resultRefresh = {};

        var onSaveFinished = function (result) {
            $scope.$emit('controlesApp:pedidoUpdate', result);
            $scope.resultRefresh = result
//            $modalInstance.close(result);
        };
        
        

        $scope.IsHidden = false;
        $scope.ShowHide = function () {            
            $scope.IsHidden = $scope.IsHidden ? false : true;
        }
        
        $scope.saveParam = function (acao, pedido) {
        	if(acao == 0){
        		pedido.statusPedido = 'EmProcessoPedido';	
        	}else if(acao == 1){
        		pedido.statusPedido = 'EmSeparacao';
        	}
        };


        $scope.save = function (pedido) {
            if (pedido.id != null) {
            	for(var i=0; i < pedido.produtosPedidos.length; i++) {
            		if(pedido.produtosPedidos[i].quantidadeNew != null){
            			pedido.produtosPedidos[i].quantidade = pedido.produtosPedidos[i].quantidadeNew;	
            		}            		 
            	}
            	if(pedido.desconto == null || isNaN(pedido.desconto) || typeof pedido.desconto == "undefined"){
        			pedido.totalDesconto =  pedido.total;
        		}
            	
                Pedido.update(pedido, onSaveFinished);
                var pos = $scope.pedidos.indexOf(pedido); 
                var remover = $scope.pedidos.splice(pos, 1);
                if($scope.pedidos.length == 0){                
                	$scope.clear(); 
                }
            }
        };
        
        $scope.showProdutos = function () {
            $('#showProdutos').modal('show');
        };
        $scope.clearProdutos = function() {    	        	          
            $('#showProdutos').modal('hide');
      }; 

        $scope.clear = function() {    	        	
//            $window.location.href = '#/pedidos';
        	$modalInstance.dismiss('cancel');
            $modalInstance.close($scope.resultRefresh);
        };                                        
}]);
