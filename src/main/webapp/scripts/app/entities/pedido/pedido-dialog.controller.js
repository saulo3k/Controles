'use strict';

angular.module('controlesApp').controller('PedidoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Pedido', 'Produto', 'User',
        function($scope, $stateParams, $modalInstance, entity, Pedido, Produto, User) {

        $scope.pedido = entity;
        $scope.produtos = Produto.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Pedido.get({id : id}, function(result) {
                $scope.pedido = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('controlesApp:pedidoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.pedido.id != null) {
                Pedido.update($scope.pedido, onSaveFinished);
            } else {
                Pedido.save($scope.pedido, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
