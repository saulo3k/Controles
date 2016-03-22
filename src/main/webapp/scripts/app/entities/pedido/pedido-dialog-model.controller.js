'use strict';

angular.module('controlesApp').controller('PedidoDialogModelController',
    ['$scope', '$stateParams', '$modalInstance', '$filter', 'entity', 'PedidoModelo', 'Produto', 'User', 'Cliente', 'DiasSemana','ClienteProduto','$q',
        function($scope, $stateParams, $modalInstance, $filter, entity, PedidoModelo, Produto, User, Cliente, DiasSemana, ClienteProduto, $q) {
    	$scope.diasSemanas = DiasSemana.query();
        $scope.pedido = entity;
        $scope.produtos = Produto.query({page: $scope.page, size: 9000});
        $scope.categorias = [];        
        $scope.isActive =[];
        $scope.clientes = Cliente.query({page: $scope.page, size: 9000, sort: "nome" + ',' + 'asc'});
        $scope.keyCode = "";
        $scope.keyPressed = function(e) {
          $scope.keyCode = e.which;
        };
                 
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
        
        $scope.calculaTotal = function (produtos) {
        	$scope.pedido.total = Number(0);
        	angular.forEach(produtos, function(valueProduto, keyProduto) {
        		var precoVenda = Number(valueProduto.precoVenda || 0);
        		var quantidade = Number(valueProduto.quantidade || 0);
        		$scope.pedido.total = $scope.pedido.total + (precoVenda  * quantidade);	
        	});
        	$scope.calcularDesconto($scope.pedido);
//        	$scope.keyPressed = function(e) {        
            if($scope.keyCode == 97){        	   
                   $scope.viewValue = {};
             	   $scope.focusInput=false;           	   
             	   $( "#pesquisa" ).focus();
             	   $scope.searchKeyword = '';
            };        
//             };
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
                $scope.focusInput = true;
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
        	
        	// Multiple async call
        	$q.all([
             	   $scope.produtos.$promise,
             	   $scope.pedido.$promise
             	]).then(function(data) {
             	   var dataProd = data[0];
             	   var data = data[1];
             	   
	    	        	for (var i = 0; i < dataProd.length; i++) {    
	    	        		var valueProduto = dataProd[i];
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
	    	                	for (var j = 0; j < data.produtosPedidos.length; j++) {    
	    		    					var valuePedido = data.produtosPedidos[j];	
    		    						if(valueProduto.id == valuePedido.produto.id) {    		    							
    		    				              $scope.selection.push(valueProduto.id);    
    		    				              $scope.produtosSelecionadosPedidos.push(valuePedido);
    		    				              valueProduto.quantidade = valuePedido.quantidade;
    		    				              console.log('Chegou');
    		    				              break;
    		    						}
	    		    			}; 
	    	        		}
	    	        		
	    	            };         	
             	   
             	});	        	    		   
        };       
        
        $scope.carregarPedido();
}]);


angular.module('controlesApp').directive('focusMe', function($timeout) {
	  return {
	    link: function(scope, element, attrs) {
	      scope.$watch(attrs.focusMe, function(value) {
	        if(value === true) { 	          
	        	if(element[0].value == ""){
	        		element[0].focus();
		            scope[attrs.focusMe] = false;	
	        	}	            
	        }
	      });
	    }
	  };
	});
angular.module('controlesApp').directive('shortcut', function() {
	  return {
		    restrict: 'E',
		    replace: true,
		    scope: true,
		    link:    function postLink(scope, iElement, iAttrs){
		      jQuery(document).on('keypress', function(e){
		         scope.$apply(scope.keyPressed(e));
		       });
		    }
		  };
});