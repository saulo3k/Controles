'use strict';

angular.module('controlesApp')
    .controller('PedTesteDetailController', function ($scope, $rootScope, $stateParams, entity, PedTeste, Produto) {
        $scope.pedTeste = entity;
        $scope.load = function (id) {
            PedTeste.get({id: id}, function(result) {
                $scope.pedTeste = result;
            });
        };
        var unsubscribe = $rootScope.$on('controlesApp:pedTesteUpdate', function(event, result) {
            $scope.pedTeste = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
