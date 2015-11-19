'use strict';

angular.module('controlesApp')
    .controller('OrdemController', function ($scope, Ordem, OrdemSearch, ParseLinks) {
        $scope.ordems = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Ordem.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.ordems.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.ordems = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Ordem.get({id: id}, function(result) {
                $scope.ordem = result;
                $('#deleteOrdemConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Ordem.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteOrdemConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            OrdemSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.ordems = result;
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
            $scope.ordem = {
                periodoPedidoInicio: null,
                periodoPedidoFim: null,
                dtPrevistaSeparacao: null,
                dtRealSeparacao: null,
                dtPrevistaEntrega: null,
                dtRealEntrega: null,
                dataPedido: null,
                id: null
            };
        };
    });
