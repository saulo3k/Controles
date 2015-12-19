'use strict';

angular.module('controlesApp')
    .controller('MainController', function ($scope, Principal, Quantidade) {
        Principal.identity().then(function(account) {
            $scope.account = account;                        
            $scope.quantidade = {};
            $scope.loadAll = function() {
            	Quantidade.query({page: $scope.page, size: 50}, function(result, headers) {
            		$scope.quantidadeCliente = result.quantidadeCliente;
            		$scope.quantidadeSeparacao = result.quantidadeSeparacao;
            		$scope.quantidadeEntrega = result.quantidadeEntrega;
            		$scope.quantidadeEstoque = result.quantidadeEstoque;
            		$scope.quantidadeUsuario = result.quantidadeUsuario;
            		$scope.quantidadeProduto = result.quantidadeProduto;
            		$scope.quantidadePedido = result.quantidadePedido;
            		$scope.quantidadeCategoria = result.quantidadeCategoria;
            	});
            };
            $scope.loadAll();
//            var response = ClienteQuantidade.get();
//            response.$promise.then(function(data){
//            	console.log(data);
//            	$scope.quantidadeClientes = Number(data); //Changed data.data.topics to data.topics
//            });
////            $scope.quantidadeClientes = ClienteQuantidCade.get();
//	        response.$promise.then(function(dataProd) {
////	        	console.log(dataProd);
//	        	angular.forEach(dataProd, function(valueProduto, keyProduto) {
////	        		console.log(valueProduto, keyProduto);
//	        		$scope.quantidadeClientes =	valueProduto;
//	        	});
////	        	
//	        });
//            console.log($scope.quantidadeClientes);
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });
