'use strict';

angular.module('controlesApp')
    .controller('PedTesteController', function ($scope, PedTeste, PedTesteSearch, ParseLinks) {
        $scope.pedTestes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            PedTeste.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.pedTestes.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.pedTestes = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PedTeste.get({id: id}, function(result) {
                $scope.pedTeste = result;
                $('#deletePedTesteConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PedTeste.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deletePedTesteConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            PedTesteSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.pedTestes = result;
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
            $scope.pedTeste = {
                dtPrevistaEntrega: null,
                dtPrevistaEntregaZoned: null,
                id: null
            };
        };
    });
