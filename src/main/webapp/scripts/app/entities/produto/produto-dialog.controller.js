'use strict';

angular.module('controlesApp').controller('ProdutoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Produto', 'CategoriaProduto', 'User',
        function($scope, $stateParams, $modalInstance, entity, Produto, CategoriaProduto, User) {

        $scope.produto = entity;
        $scope.categoriaprodutos = CategoriaProduto.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Produto.get({id : id}, function(result) {
                $scope.produto = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('controlesApp:produtoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.produto.id != null) {
                Produto.update($scope.produto, onSaveFinished);
            } else {
                Produto.save($scope.produto, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
