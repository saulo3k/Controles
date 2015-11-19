'use strict';

angular.module('controlesApp')
    .controller('PedidoEntregaController', function ($scope, Pedido, PedidoSearch, PedidoEntrega, ParseLinks) {
        $scope.pedidos = [];
        $scope.page = 0;
        $scope.loadAll = function() {
        	PedidoEntrega.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.pedidos.push(result[i]);
                }
            });
        };
		    	
        $scope.reset = function() {
            $scope.page = 0;
            $scope.pedidos = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();
    

        $scope.search = function () {
            PedidoSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.pedidos = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.pedido = {
                dtPrevistaSeparacao: null,
                dtRealSeparacao: null,
                dtPrevistaEntrega: null,
                dtRealEntrega: null,
                periodoPedidoInicio: null,
                periodoPedidoFim: null,
                dataPedido: null,
                id: null
            };
        };
    });
