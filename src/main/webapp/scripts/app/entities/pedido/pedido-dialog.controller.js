'use strict';

angular.module('controlesApp').controller('PedidoDialogController',
    ['$scope', '$stateParams', '$modalInstance',  'entity', 'Pedido', 'Produto', 'User', 'Cliente',
        function($scope, $stateParams, $modalInstance, entity, Pedido, Produto, User, Cliente) {
    	
        $scope.pedido = entity;        
        $scope.produtos = Produto.query();
        $scope.users = User.query();
        $scope.clientes = Cliente.query();
        $scope.load = function(id) {
            Pedido.get({id : id}, function(result) {            	
                $scope.pedido = result;
            });                         
        };                       
                   
        $scope.selection = [];
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
                }
                
                ;
            }
        };
        $scope.pedido.total = 0;
        $scope.setQuantidade = function (idx, produto) {
        	var produtoPedido = {produto: produto, pedido: {}, quantidade: produto.quantidade};        		
        		if($scope.pedido.id != null) {
        			console.log('atualizar');
        			for (var i=0; i < $scope.pedido.produtosPedidos.length; i++) {
        				if($scope.pedido.produtosPedidos[i].produto.id == produto.id) {
        	    			console.log('apagou');
        	    			console.log($scope.pedido.produtosPedidos.splice(i, 1));        					
        				}        			
        			}        	
        		}
        		$scope.pedido.produtosPedidos.push(produtoPedido);
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
        
      
        $scope.carregarPedido = function () {        		
        	
        	$scope.produtosSelecionadosPedidos = [];        
        	
	        $scope.produtos.$promise.then(function(dataProd) {
	            
	        	angular.forEach(dataProd, function(valueProduto, keyProduto) {
	        		
	        		if($scope.pedido.id != null){
		        		$scope.pedido.$promise.then(function(data) {
		    					angular.forEach(data.produtosPedidos, function(valuePedido, key) {
		    						if(valueProduto.id == valuePedido.produto.id) {	    							  
		    				              $scope.selection.push(keyProduto);    
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
