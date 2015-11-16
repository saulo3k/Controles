'use strict';

angular.module('controlesApp')
    .controller('PedidoDetailController', function ($scope, $rootScope, $stateParams, entity, Pedido, Produto, User, Cliente) {
        $scope.pedido = entity;
        $scope.load = function (id) {
            Pedido.get({id: id}, function(result) {
                $scope.pedido = result;
            });
        };
        var unsubscribe = $rootScope.$on('controlesApp:pedidoUpdate', function(event, result) {
            $scope.pedido = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
