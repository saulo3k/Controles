'use strict';

angular.module('controlesApp').controller('PedidoEntregaDialogController',
    ['$scope', '$stateParams',  'entity', 'Pedido', 'Produto', 'PedidoEntrega','ClienteProduto', 
        function($scope, $stateParams, entity, Pedido, Produto, PedidoEntrega, ClienteProduto) {
    	 
        $scope.pedido = null; 
        
        $scope.produtos = Produto.query({page: $scope.page, size: 7000});
        $scope.load = function(id) {
            Pedido.get({id : id}, function(result) {            	
                $scope.pedido = result;
                $scope.buscarPrecosExclusivos(result.cliente_pedido.id);
                $scope.carregarPedido(result);            
            });                         
        };         
        $scope.load(entity);
        $scope.puxarPedidos= function() {        	
        	Pedido.get({id : id, size: 3000}, function(result) {            	
                $scope.pedido = result;
            });                        
        }
        
        $scope.separados = [];           
        $scope.selection = [];
        $scope.produtosSelecionadosPedidos = [];        
        

        var onSaveFinished = function (result) {
            $scope.$emit('controlesApp:pedidoUpdate', result);            
        };               

        $scope.save = function () {    		            
            $scope.pedido.statusPedido = 'SaiuParaRomaneio';         
        	PedidoEntrega.updateEntrega($scope.pedido, onSaveFinished);        	
        };
        
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        
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
        
        $scope.carregarPedido = function (pedido) {        		
        	
        	$scope.produtosSelecionadosPedidos = [];        
        	        	        	        	
	        $scope.produtos.$promise.then(function(dataProd) {
	            
	        	angular.forEach(dataProd, function(valueProduto, keyProduto) {	        		
	    					angular.forEach(pedido.produtosPedidos, function(valuePedido, key) {
	    						if(valueProduto.id == valuePedido.produto.id) {	    						
	    				              valueProduto.quantidade = valuePedido.quantidade;	    				              
	    				              $scope.separados.push(valueProduto);
	    						}
	    					});	    			
	            });    	        	        	
	        });	    	        	        
        };       
        
            
}]);
