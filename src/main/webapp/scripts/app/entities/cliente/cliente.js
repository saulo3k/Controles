'use strict';

angular.module('controlesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('cliente', {
                parent: 'entity',
                url: '/clientes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.cliente.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/cliente/clientes.html',
                        controller: 'ClienteController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('cliente');
                        $translatePartialLoader.addPart('tipoTelefone');
                        $translatePartialLoader.addPart('estado');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('cliente.produto', {
                parent: 'cliente',
                url: '/cliente/produto/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/cliente/cliente-produto-dialog.html',
                        controller: 'ClienteProdutoDialogController',
                        size: 'lg',                        
                        resolve: {
                        	 entity: function () {
                                 return $stateParams.id;
                              }
                        }
                    }).result.then(function(result) {
                        $state.go('cliente', null, { reload: true });
                    }, function() {
                        $state.go('cliente');
                    })
                }]
            })
            .state('cliente.detail', {
                parent: 'entity',
                url: '/cliente/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.cliente.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/cliente/cliente-detail.html',
                        controller: 'ClienteDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('cliente');
                        $translatePartialLoader.addPart('tipoTelefone');
                        $translatePartialLoader.addPart('estado');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Cliente', function($stateParams, Cliente) {
                        return Cliente.get({id : $stateParams.id});
                    }]
                }
            })
            .state('cliente.new', {
                parent: 'cliente',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/cliente/cliente-dialog.html',
                        controller: 'ClienteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
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
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('cliente', null, { reload: true });
                    }, function() {
                        $state.go('cliente');
                    })
                }]
            })
            .state('cliente.edit', {
                parent: 'cliente',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/cliente/cliente-dialog.html',
                        controller: 'ClienteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Cliente', function(Cliente) {
                                return Cliente.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('cliente', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
