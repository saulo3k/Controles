'use strict';

angular.module('controlesApp')
    .controller('CategoriaProdutoController', function ($scope, CategoriaProduto, CategoriaProdutoSearch, ParseLinks) {
        $scope.categoriaProdutos = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            CategoriaProduto.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.categoriaProdutos.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.categoriaProdutos = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            CategoriaProduto.get({id: id}, function(result) {
                $scope.categoriaProduto = result;
                $('#deleteCategoriaProdutoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CategoriaProduto.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteCategoriaProdutoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            CategoriaProdutoSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.categoriaProdutos = result;
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
            $scope.categoriaProduto = {
                nome: null,
                descricao: null,
                ativa: false,
                id: null
            };
        };
    });
