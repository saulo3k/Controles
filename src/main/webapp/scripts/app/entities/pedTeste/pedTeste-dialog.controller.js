'use strict';

angular.module('controlesApp').controller('PedTesteDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'PedTeste', 'Produto',
        function($scope, $stateParams, $modalInstance, entity, PedTeste, Produto) {

        $scope.pedTeste = entity;
        $scope.produtos = Produto.query();
        $scope.load = function(id) {
            PedTeste.get({id : id}, function(result) {
                $scope.pedTeste = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('controlesApp:pedTesteUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.pedTeste.id != null) {
                PedTeste.update($scope.pedTeste, onSaveFinished);
            } else {
                PedTeste.save($scope.pedTeste, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
