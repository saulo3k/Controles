'use strict';

angular.module('controlesApp')
    .controller('MainController', function ($scope, Principal, Produto, Pedido, ClienteQuantidade, CategoriaProduto, Estoque) {
        Principal.identity().then(function(account) {
            $scope.account = account;                        
            
            var response = ClienteQuantidade.get();
            response.$promise.then(function(data){
            	$scope.quantidadeClientes = Number(data); //Changed data.data.topics to data.topics
            });
//            $scope.quantidadeClientes = ClienteQuantidCade.get();
	        response.$promise.then(function(dataProd) {
//	        	console.log(dataProd);
	        	angular.forEach(dataProd, function(valueProduto, keyProduto) {
//	        		console.log(valueProduto, keyProduto);
	        		$scope.quantidadeClientes =	valueProduto;
	        	});
//	        	
	        });
//            console.log($scope.quantidadeClientes);
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });
