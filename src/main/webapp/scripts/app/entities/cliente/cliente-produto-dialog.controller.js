'use strict';

angular.module('controlesApp').controller('ClienteProdutoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Cliente', 'Produto', 'ClienteProduto', 'ParseLinks',
        function($scope, $stateParams, $modalInstance, entity, Cliente, Produto, ClienteProduto, ParseLinks) {
        
    	$scope.produtos = Produto.query({page: $scope.page, size: 7000});
        $scope.selection = [];
        $scope.selectionProdutos = [];
    	$scope.clienteProdutos = [];
    	    
        
	    $scope.loadAllClienteProdutos = function() {
	    	
	    	var produtoClienteTratamento = [];
	    	
	    	ClienteProduto.get({id : entity}, function(result, headers) {    			
                
	    		for (var i = 0; i < result.length; i++) {
                	$scope.clienteProdutos.push(result[i]);        			
        			produtoClienteTratamento.push(result[i]);        			        	        
                }
	    		
	    		$scope.produtos.$promise.then(function(dataProduto) {
	    			angular.forEach(dataProduto, function(valueProduto, indexProduto) {
	    				valueProduto.precoAnterior = valueProduto.precoVenda;
	    				angular.forEach(produtoClienteTratamento, function(clienteProduto, indexClienteProduto) {	    					
	        				if (clienteProduto.produto.id == valueProduto.id) {
	        					$scope.selection.push(indexProduto);	        					
	        					valueProduto.precoVenda = clienteProduto.precoVenda;
	        					$scope.selectionProdutos.push(valueProduto);
	        				}
	    				});
	        		});
	        	});
	        	
            });
	    	
        };
        
        $scope.loadAllClienteProdutos();
        
    	
        $scope.toggle = function (idx, produto) {
            var pos = $scope.selection.indexOf(idx);
            if (pos == -1) {
                $scope.selection.push(idx);                
                $scope.selectionProdutos.push(produto);
            }else{            	
            	$scope.selection.splice(pos, 1);   
                var remover = $scope.selectionProdutos.indexOf(produto);  
                if(remover > -1) {                		
                	$scope.selectionProdutos.splice(remover, 1);
                }	                
            }
        }    	       

        var onSaveFinished = function (result) {
            $scope.$emit('controlesApp:clienteUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
        	var cliente = {id: entity};
        	ClienteProduto.delete({id: entity}, function () {
        		angular.forEach($scope.selectionProdutos, function(valueProduto, indexProduto) {        		        	
                    var clienteProduto = {cliente: cliente, produto: valueProduto, precoVenda: valueProduto.precoVenda};   
                    ClienteProduto.update(clienteProduto, onSaveFinished);               
            	});
            });        	
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        
        
}]);
