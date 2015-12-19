'use strict';

angular.module('controlesApp').controller('ClienteProdutoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Cliente', 'Produto', 'ClienteProduto',
        function($scope, $stateParams, $modalInstance, entity, Cliente, Produto, ClienteProduto) {
        
    	$scope.produtos = Produto.query();
        $scope.selection = [];
        $scope.selectionProdutos = [];
    	$scope.clienteProdutos = [];
    	
	    $scope.loadAll = function() {
	    	var produtoClienteTratamento = [];
	    	ClienteProduto.get({id : entity}, function(result, headers) {
    			console.log('sizeclienteProdutos', result.length);
                for (var i = 0; i < result.length; i++) {                	
                 	
                	$scope.clienteProdutos.push(result[i]);        			
        			produtoClienteTratamento.push(result[i]);        			        	        
                }
                $scope.produtos.$promise.then(function(dataProd) {
        			angular.forEach(dataProd, function(valueProduto, indexProduto) {
        				angular.forEach(produtoClienteTratamento, function(clienteProduto, indexClienteProduto) {
	        				if (clienteProduto.produto.id == valueProduto.id) {
	        		            $scope.selection.push(indexProduto);    
	        	                valueProduto.precoVenda = clienteProduto.precoVenda;
	        	                console.log('valueproduto', clienteProduto.precoVenda);
	        				}
        				});
        			}); 
        			
    	        });
            });
        };
        

        
    	$scope.selecionarProdutosCarregamento = function() {
//			console.log('size', $scope.clienteProdutos.length);
//			console.log('sizeProdutos', $scope.produtos.length);
////    		angular.forEach($scope.produtos, function(valueProduto, indexProduto) {
//    			angular.forEach($scope.clienteProdutos, function(valueClienteProduto, indexClienteProduto) {
//    				console.log('entrou no for 1', valueClienteProduto);
//    			});
////    		});
    	};
    	
    	$scope.selecionarProdutosCarregamento();
    	
        $scope.toggle = function (idx, produto) {
            var pos = $scope.selection.indexOf(idx);
            if (pos == -1) {
                $scope.selection.push(idx);    
                $scope.selectionProdutos.push(produto);
            }else{            	
            	$scope.selection.splice(pos, 1);   
                var remover = $scope.selectionProdutos.indexOf(produto);  
                if(remover > -1){                		
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
        	angular.forEach($scope.selectionProdutos, function(valueProduto, indexProduto) {
        		console.log('Prod',valueProduto.precoVenda);        		
                var clienteProduto = {cliente: cliente, produto: valueProduto, precoVenda: valueProduto.precoVenda};
            	console.log('marconi',clienteProduto);   
                ClienteProduto.update(clienteProduto, onSaveFinished);               
        	});
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.loadAll();
}]);
