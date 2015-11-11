'use strict';

angular.module('controlesApp')
    .controller('CategoriaProdutoDetailController', function ($scope, $rootScope, $stateParams, entity, CategoriaProduto) {
        $scope.categoriaProduto = entity;
        $scope.load = function (id) {
            CategoriaProduto.get({id: id}, function(result) {
                $scope.categoriaProduto = result;
            });
        };
        var unsubscribe = $rootScope.$on('controlesApp:categoriaProdutoUpdate', function(event, result) {
            $scope.categoriaProduto = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
