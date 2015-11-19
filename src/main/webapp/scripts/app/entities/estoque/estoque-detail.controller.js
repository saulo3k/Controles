'use strict';

angular.module('controlesApp')
    .controller('EstoqueDetailController', function ($scope, $rootScope, $stateParams, entity, Estoque, Produto, User) {
        $scope.estoque = entity;
        $scope.load = function (id) {
            Estoque.get({id: id}, function(result) {
                $scope.estoque = result;
            });
        };
        var unsubscribe = $rootScope.$on('controlesApp:estoqueUpdate', function(event, result) {
            $scope.estoque = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
