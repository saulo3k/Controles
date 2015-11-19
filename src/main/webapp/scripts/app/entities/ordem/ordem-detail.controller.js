'use strict';

angular.module('controlesApp')
    .controller('OrdemDetailController', function ($scope, $rootScope, $stateParams, entity, Ordem, Produto, Cliente, User) {
        $scope.ordem = entity;
        $scope.load = function (id) {
            Ordem.get({id: id}, function(result) {
                $scope.ordem = result;
            });
        };
        var unsubscribe = $rootScope.$on('controlesApp:ordemUpdate', function(event, result) {
            $scope.ordem = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
