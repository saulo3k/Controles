'use strict';

angular.module('controlesApp')
    .controller('ProdutoController', function ($scope, Produto, ProdutoSearch, ParseLinks) {
        $scope.produtos = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Produto.query({page: $scope.page, size: 2000, sort: "nome" + ',' + 'asc'}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.produtos.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.produtos = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Produto.get({id: id}, function(result) {
                $scope.produto = result;
                $('#deleteProdutoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Produto.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteProdutoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ProdutoSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.produtos = result;
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
            $scope.produto = {
                nome: null,
                descricao: null,
                referencia: null,
                codigoBarras: null,
                precoCusto: null,
                precoVenda: null,
                estoque: null,
                vendaSemEstoque: null,
                promocao: null,
                dataCadastro: null,
                unidadeMedida: null,
                id: null
            };
        };
        $scope.isActiveUpload = false;
        $scope.changeColorSelect = function(name) {
//        	
            if($scope.isActiveUpload){            
        		$scope.isActiveUpload = false;        		        		
        		$scope.change = 'btn-primary'; 
            }else{            
                $scope.isActiveUpload = true;
                $scope.change = 'btn-success';
            }
            
//            $scope.change = 
//            {'btn-primary':!$scope.isActiveUpload,  'btn-success':$scope.isActiveUpload};
    	};
        
    });
