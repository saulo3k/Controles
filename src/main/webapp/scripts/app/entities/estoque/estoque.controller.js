'use strict';

angular.module('controlesApp')
    .controller('EstoqueController', function ($scope, Estoque, EstoqueSearch, ParseLinks) {
    	
        $scope.estoques = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Estoque.query({page: $scope.page, size: 20, sort: "id" + ',' + 'desc'}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.estoques.push(result[i]);
                }
            });
        };
        
        $scope.puxarPedidos= function() {        	
        	Estoque.query({page: $scope.page, size: 7000}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.estoques.push(result[i]);
                }
            });                        
        }
        
        $scope.loadAllParse = function() {
            Estoque.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.estoques.push(result[i]);
                }
            });
        };
        
        $scope.reset = function() {
            $scope.page = 0;
            $scope.estoques = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAllParse();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Estoque.get({id: id}, function(result) {
                $scope.estoque = result;
                $('#deleteEstoqueConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Estoque.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteEstoqueConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            EstoqueSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.estoques = result;
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
            $scope.estoque = {
                quantidadeAtual: null,
                quantidadeAposMovimentacao: null,
                dataAtual: null,
                operacao: null,
                motivo: null,
                id: null
            };
        };        
    });
