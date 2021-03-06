'use strict';

angular.module('controlesApp').controller('PedidoSeparacaoDialogController',
    ['$scope', '$stateParams', '$modalInstance',  'entity', 'Pedido', 'Produto', 'User', 'Cliente', 'PedidoSeparacao',
        function($scope, $stateParams, $modalInstance, entity, Pedido, Produto, User, Cliente, PedidoSeparacao) {
    	
        $scope.pedido = entity;        
        $scope.produtos = Produto.query({page: $scope.page, size: 7000});
        $scope.users = User.query();
        $scope.clientes = Cliente.query({page: $scope.page, size: 9000});
        $scope.load = function(id) {
            Pedido.get({id : id}, function(result) {            	
                $scope.pedido = result;
            });                         
        };                       
        $scope.separados = [];           
        $scope.selection = [];
        $scope.selectionFirst = [];
        $scope.produtosSelecionadosPedidos = [];        
                
        $scope.toggle = function (idx, produto) {
            var pos = $scope.selection.indexOf(idx);                        
            var posAux = $scope.selectionFirst.indexOf(produto.id);         
            
            if(posAux == -1){            	
            	$scope.selectionFirst.push(produto.id);
            	
            	return false;
            }
            if (posAux != -1 && pos == -1) {            	
                $scope.selection.push(idx);
                $scope.produtosSelecionadosPedidos.push(produto);
                return false;
                
            } 
            	
        	$scope.selectionFirst.splice(posAux, 1);
	        	if (pos != -1) {	
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

        $scope.IsHidden = false;
        $scope.ShowHide = function () {            
            $scope.IsHidden = $scope.IsHidden ? false : true;
        }

        $scope.save = function () {
        	if($scope.separados.length != $scope.produtosSelecionadosPedidos.length) {
        	        		
        		$('#restantePedidoConfirmation').modal('show');
        		
        	}else{        		
            	$scope.pedido.statusPedido = 'Romaneio';            	
                PedidoSeparacao.updateSeparacao($scope.pedido, onSaveFinished);
        	}        	
        };
        
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        
        $scope.fecharAlerta = function() {
        	$('#restantePedidoConfirmation').modal('hide');
        };
        $scope.mostrarVerdura = false;
        $scope.mostrarLegumes = false;
        $scope.carregarPedido = function () {        		

        	$scope.separados = [];                	
	        $scope.produtos.$promise.then(function(dataProd) {
	            
	        	angular.forEach(dataProd, function(valueProduto, keyProduto) {	        		
	        		$scope.pedido.$promise.then(function(data) {
	    					angular.forEach(data.produtosPedidos, function(valuePedido, key) {
	    						if(valueProduto.id == valuePedido.produto.id && $scope.separados.indexOf(valueProduto) == -1) {	    												
	    				              valueProduto.quantidade = valuePedido.quantidade;	    				              
	    				              $scope.separados.push(valueProduto);
	    				              if(valueProduto.categoriaProduto.nome == 'Verduras'){
	    				            	  $scope.mostrarVerdura = true;				            	  
	    				              }
	    				              if(valueProduto.categoriaProduto.nome == 'Legumes'){
	    				            	  $scope.mostrarLegumes = true;
	    				              }
	    						}
	    					});
	    			});	        		
	            });    	        	        	
	        });	    	 	        
        };       
        
        $scope.carregarPedido();
        
        $scope.iniciarSeparacao = function () {        	         	 
            PedidoSeparacao.updateSeparacao($scope.pedido);            
        };

}]);
