'use strict';

angular.module('controlesApp').controller('CategoriaProdutoDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CategoriaProduto',
        function($scope, $stateParams, $modalInstance, entity, CategoriaProduto) {

        $scope.categoriaProduto = entity;
        $scope.load = function(id) {
            CategoriaProduto.get({id : id}, function(result) {
                $scope.categoriaProduto = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('controlesApp:categoriaProdutoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.categoriaProduto.id != null) {
                CategoriaProduto.update($scope.categoriaProduto, onSaveFinished);
            } else {
                CategoriaProduto.save($scope.categoriaProduto, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
