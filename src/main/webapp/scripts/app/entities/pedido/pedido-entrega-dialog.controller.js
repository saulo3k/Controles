'use strict';

angular.module('controlesApp').controller('PedidoEntregaDialogController',
    ['$scope', '$stateParams', '$modalInstance',  'entity', 'Pedido', 'Produto', 'User', 'Cliente', 'PedidoEntrega',
        function($scope, $stateParams, $modalInstance, entity, Pedido, Produto, User, Cliente, PedidoEntrega) {
    	
        $scope.pedido = entity;        
        $scope.produtos = Produto.query();
        $scope.users = User.query();
        $scope.clientes = Cliente.query();
        $scope.load = function(id) {
            Pedido.get({id : id}, function(result) {            	
                $scope.pedido = result;
            });                         
        };                       
        $scope.separados = [];           
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
            $scope.pedido.statusPedido = 'SaiuParaRomaneio';         
        	PedidoEntrega.updateEntrega($scope.pedido, onSaveFinished);        	
        };
        
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        
        
        $scope.carregarPedido = function () {        		
        	
        	$scope.produtosSelecionadosPedidos = [];        
        	
	        $scope.produtos.$promise.then(function(dataProd) {
	            
	        	angular.forEach(dataProd, function(valueProduto, keyProduto) {	        		
	        		$scope.pedido.$promise.then(function(data) {
	    					angular.forEach(data.produtosPedidos, function(valuePedido, key) {
	    						if(valueProduto.id == valuePedido.produto.id) {	    						
	    				              valueProduto.quantidade = valuePedido.quantidade;	    				              
	    				              $scope.separados.push(valueProduto);
	    						}
	    					});
	    			});	        		
	            });    	        	        	
	        });	    	        	        
        };       
        
        $scope.carregarPedido();
}]);
