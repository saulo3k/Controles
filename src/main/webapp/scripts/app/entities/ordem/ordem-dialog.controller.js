'use strict';

angular.module('controlesApp').controller('OrdemDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Ordem', 'Produto', 'Cliente', 'User',
        function($scope, $stateParams, $modalInstance, entity, Ordem, Produto, Cliente, User) {

        $scope.ordem = entity;
        $scope.produtos = Produto.query();
        $scope.clientes = Cliente.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Ordem.get({id : id}, function(result) {
                $scope.ordem = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('controlesApp:ordemUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.ordem.id != null) {
                Ordem.update($scope.ordem, onSaveFinished);
            } else {
                Ordem.save($scope.ordem, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
