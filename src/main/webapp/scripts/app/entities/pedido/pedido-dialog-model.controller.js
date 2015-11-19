'use strict';

angular.module('controlesApp').controller('PedidoDialogModelController',
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
        
        $scope.validaArray = function() {        
//        	console.log($scope.produtos);
        }        		
                
        $scope.carregarPedido = function () {        	
        	$scope.produtos.forEach(function (item, index, array) {
        		  console.log(item, index);
        	});
        };       
        
        
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
        
        $scope.setQuantidade = function (idx, quantidadeVal, produto) {                  		        		
        		var produtoPedido = {produto: produto, pedido: {}, quantidade: quantidadeVal}
        		$scope.pedido.produtosPedidos.push(produtoPedido);
        };  
                 
        
        $scope.listaOpcoes = [{
        	    "id": false,
        	    "description": "Não",
        	    "name": "Não"
        	},
        	    {
        	    "id": true,
        	    "description": "Sim",
        	    "name": "Sim"
        	}] ;
        

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
        $scope.validaArray();
        $scope.carregarPedido();
}]);
