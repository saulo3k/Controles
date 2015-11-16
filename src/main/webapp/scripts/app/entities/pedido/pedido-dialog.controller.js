'use strict';

angular.module('controlesApp').controller('PedidoDialogController',		
		
        function($scope, $rootScope, $stateParams, enity, Pedido, Produto, User, Cliente) {
        $scope.pedido = [];
        $scope.produtos = Produto.query();
        $scope.users = User.query();
        $scope.clientes = Cliente.query();
        $scope.load = function(id) {
            Pedido.get({id : id}, function(result) {
                $scope.pedido = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('controlesApp:pedidoUpdate', result);
//            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.pedido.id != null) {
                Pedido.update($scope.pedido, onSaveFinished);
            } else {            	
                Pedido.save($scope.pedido, onSaveFinished);
            }
        };

        $scope.clear = function() {
//            $modalInstance.dismiss('cancel');
        };
});
