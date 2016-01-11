'use strict';

angular.module('controlesApp')
    .controller('ClienteController', function ($scope, Cliente, ClienteSearch, ParseLinks) {
        $scope.clientes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Cliente.query({page: $scope.page, size: 20, sort: "nome" + ',' + 'asc'}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.clientes.push(result[i]);
                }
            });
        };
                
        $scope.reset = function() {
            $scope.page = 0;
            $scope.clientes = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Cliente.get({id: id}, function(result) {
                $scope.cliente = result;
                $('#deleteClienteConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Cliente.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteClienteConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ClienteSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.clientes = result;
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
            $scope.cliente = {
                nome: null,
                tipoTelefone: null,
                telefone: null,
                cpfCnpj: null,
                email: null,
                cep: null,
                endereco: null,
                estado: null,
                cidade: null,
                nomeContato: null,
                informacoesParaBusca: null,
                id: null
            };
        };
    });
