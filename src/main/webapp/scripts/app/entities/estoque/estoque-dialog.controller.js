'use strict';

angular.module('controlesApp').controller('EstoqueDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Estoque', 'Produto', 'User',
        function($scope, $stateParams, $modalInstance, entity, Estoque, Produto, User) {

        $scope.estoque = entity;
        $scope.produtos = Produto.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Estoque.get({id : id}, function(result) {
                $scope.estoque = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('controlesApp:estoqueUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.estoque.id != null) {
                Estoque.update($scope.estoque, onSaveFinished);
            } else {
                Estoque.save($scope.estoque, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
