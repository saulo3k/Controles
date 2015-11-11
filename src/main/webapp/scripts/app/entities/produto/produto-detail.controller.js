'use strict';

angular.module('controlesApp')
    .controller('ProdutoDetailController', function ($scope, $rootScope, $stateParams, entity, Produto, CategoriaProduto, User) {
        $scope.produto = entity;
        $scope.load = function (id) {
            Produto.get({id: id}, function(result) {
                $scope.produto = result;
            });
        };
        var unsubscribe = $rootScope.$on('controlesApp:produtoUpdate', function(event, result) {
            $scope.produto = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
