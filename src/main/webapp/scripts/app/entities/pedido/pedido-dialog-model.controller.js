'use strict';

angular.module('controlesApp').controller('PedidoDialogModelController',
    ['$scope', '$stateParams', '$modalInstance', '$filter', 'entity', 'PedidoModelo', 'Produto', 'User', 'Cliente', 'DiasSemana','ClienteProduto',
        function($scope, $stateParams, $modalInstance, $filter, entity, PedidoModelo, Produto, User, Cliente, DiasSemana, ClienteProduto) {
    	$scope.diasSemanas = DiasSemana.query();
        $scope.pedido = entity;
        $scope.produtos = Produto.query({page: $scope.page, size: 9000});
        $scope.categorias = [];        
        $scope.isActive =[];
        $scope.clientes = Cliente.query({page: $scope.page, size: 9000, sort: "nome" + ',' + 'asc'});
                 
        $scope.load = function(id) {
        	PedidoModelo.get({id : id}, function(result) {            	
                $scope.pedido = result;
            });                         
        };
        $scope.calcularDesconto = function (pedido) {
        	if(typeof pedido.desconto == "undefined"){        		
        		pedido.totalDesconto =  pedido.total;
        	}else{
        		pedido.totalDesconto =  pedido.total - pedido.desconto;
        	}  
        };
        
        $scope.puxarPedidos= function() {        	
        	PedidoModelo.get({id : id, size: 3000}, function(result) {            	
                $scope.pedido = result;
            });
        }
        
        $scope.clienteProdutos = [];
        $scope.buscarPrecosExclusivos = function(id){			
        	ClienteProduto.get({id : id}, function(result, headers) {
                for (var i = 0; i < result.length; i++) {                
                	var clienteProd = result[i];
                	$scope.clienteProdutos.push(clienteProd);               
                	var keepGoing = true;
                	
                	for (var j = 0; j < $scope.produtos.length; j++) {
                			var valueProduto = $scope.produtos[j];
            				if(keepGoing){
	            				if(valueProduto.id == clienteProd.produto.id){
		    						valueProduto.precoVenda = clienteProd.precoVenda;		    						
		    						keepGoing = false;
		    					}
            				}
                	}	                	
                }
        	});
        };    
        
        $scope.selection = [];
        $scope.produtosSelecionadosPedidos = [];        
        
        $scope.somar = function (produto) {        
        	for (var i=0; i < $scope.pedido.produtosPedidos.length; i++) {
        		if($scope.pedido.produtosPedidos[i].produto.id == produto.id) {
        			if(produto.quantidade == null){
                		produto.quantidade = 1;
                		$scope.pedido.produtosPedidos[i].quantidade = 1;
                	}else{        		
                		produto.quantidade = produto.quantidade+1;
                		$scope.pedido.produtosPedidos[i].quantidade = produto.quantidade
                	}       		
        		}
        	}        	        	  
        }
        
        $scope.subtrair = function (produto) {
        	
        	for (var i=0; i < $scope.pedido.produtosPedidos.length; i++) {
        		if($scope.pedido.produtosPedidos[i].produto.id == produto.id) {
        			if(produto.quantidade == null){
                		produto.quantidade = 0;
                		$scope.pedido.produtosPedidos[i].quantidade = 0;
                	}else{        		
                		produto.quantidade = produto.quantidade-1;
                		$scope.pedido.produtosPedidos[i].quantidade = produto.quantidade
                	}       		
        		}
        	}        	        	  
        }
                
        $scope.toggle = function (idx, produto, pedido) {
            var pos = $scope.selection.indexOf(idx);
            
            if (pos == -1) {
                $scope.selection.push(idx);          
                var produtoPedido = {produto: produto, pedido: {}, quantidade: produto.quantidade};                                     
                $scope.produtosSelecionadosPedidos.push(produtoPedido);
                $scope.pedido.produtosPedidos.push(produtoPedido);
            } else {
                $scope.selection.splice(pos, 1);                                
                var remover = $scope.produtosSelecionadosPedidos.splice(pos, 1);
                
                for (var i=0; i < $scope.pedido.produtosPedidos.length; i++) {
                	if($scope.pedido.produtosPedidos[i].produto.id == produto.id){
                		produto.quantidade = null;
                		$scope.pedido.produtosPedidos.splice(i, 1);
                	}	
                }
                 
            }
            $scope.calculaTotal($scope.produtos);
        };
        
        $scope.pedido.total = 0;
        $scope.setQuantidade = function (produto) {        	
        	console.log(produto);
        	if(typeof produto.quantidade == "undefined") {
        		produto.quantidade = null;
        		return false;
        	}
			for (var i=0; i < $scope.pedido.produtosPedidos.length; i++) {
				
				if($scope.pedido.produtosPedidos[i].produto.id == produto.id) {
					$scope.pedido.produtosPedidos[i].quantidade = produto.quantidade;        					
				}        			
			}        	
        };
        
        $scope.calculaTotal = function (produtos) {
        	$scope.pedido.total = Number(0);
        	angular.forEach(produtos, function(valueProduto, keyProduto) {
        		var precoVenda = Number(valueProduto.precoVenda || 0);
        		var quantidade = Number(valueProduto.quantidade || 0);
        		$scope.pedido.total = $scope.pedido.total + (precoVenda  * quantidade);	
        	});
                   	
        }                
                
        
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
                
        $scope.save = function () {        	
        	$scope.pedido.statusPedido = $scope.status;
        	if($scope.produtosSelecionadosPedidos.length <= 0) {
        		alert('Deve existir no minimo um produto selecionado !');
        		$scope.editForm.$submitted = false;
        	}else{        		        	
       		var salvar = true;
        		for (var i = 0; i < $scope.produtosSelecionadosPedidos.length; i++) {        
        			 if($scope.produtosSelecionadosPedidos[i].quantidade < 1 || $scope.produtosSelecionadosPedidos[i].quantidade == null){
        				 alert('Existem produtos sem informar a quantidade !');		 
        				 $scope.editForm.$submitted = false;
        				 salvar = false;
        			 }
        		}
        		if($scope.pedido.desconto == null || isNaN($scope.pedido.desconto) || typeof $scope.pedido.desconto == "undefined"){
        			$scope.pedido.totalDesconto =  $scope.pedido.total;
        		}
        		if(salvar){
		            if ($scope.pedido.id != null) {            	
		                PedidoModelo.update($scope.pedido, onSaveFinished);
		            } else {            	
		                PedidoModelo.save($scope.pedido, onSaveFinished)		                
		            }		            		            
        		}        	
        	} 
        };
        
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        
        $scope.filtrarCategoria = function (categoria, ind) {        	
        	
        	if(categoria == "Todos os Produtos"){
        		$scope.category = null;	
        	}else{
        		$scope.category = categoria;
        	}
        	for(var i=0; i < $scope.isActive.length; i++){
      		  $scope.isActive[i] = i===ind;        	      
        	}
        }
        
        $scope.carregarPedido = function () {        		
        	
        	$scope.produtosSelecionadosPedidos = [];   

        	var entrada = true;
        	
		    $scope.produtos.$promise.then(function(dataProd) {
		            
		        	angular.forEach(dataProd, function(valueProduto, keyProduto) {
		        		
		        		
		        		if($scope.categorias.length == 0){
		        			$scope.categorias.push("Todos os Produtos");
		        			$scope.isActive.push(true);
		        		}
		        		
		        		if($scope.categorias.indexOf(valueProduto.categoriaProduto.nome) == -1){
		        			$scope.categorias.push(valueProduto.categoriaProduto.nome);
		        			$scope.isActive.push(false);
		        		}
		        		
			        		if($scope.pedido.id != null) {
				        		
			        			if(entrada) {	        				
				        			entrada = false;
				        			$scope.buscarPrecosExclusivos($scope.pedido.cliente_pedido.id);
				        		}
				        		
				        		$scope.pedido.$promise.then(function(data) {
				    					angular.forEach(data.produtosPedidos, function(valuePedido, key) {
				    						if(valueProduto.id == valuePedido.produto.id) {	    							  
				    				              $scope.selection.push(valueProduto.id);    
				    				              $scope.produtosSelecionadosPedidos.push(valuePedido);
				    				              valueProduto.quantidade = valuePedido.quantidade;
				    						}
				    					});
				    			});			        		
			        		}		
			         });		        	
		        });
        };       
        
        $scope.carregarPedido();

}]);
angular.module('controlesApp').directive('focusOnShow', function($timeout) {
    return {
        restrict: 'A',
        link: function($scope, $element, $attr) {
            if ($attr.ngShow){
                $scope.$watch($attr.ngShow, function(newValue){
                    if(newValue){
                        $timeout(function(){
                            $element.focus();
                        }, 0);
                    }
                })      
            }
            if ($attr.ngHide){
                $scope.$watch($attr.ngHide, function(newValue){
                    if(!newValue){
                        $timeout(function(){
                            $element.focus();
                        }, 0);
                    }
                })      
            }

        }
    };
});