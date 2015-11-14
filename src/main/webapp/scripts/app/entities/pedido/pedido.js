'use strict';

angular.module('controlesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pedido', {
                parent: 'entity',
                url: '/pedidos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.pedido.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pedido/pedidos.html',
                        controller: 'PedidoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pedido');
                        $translatePartialLoader.addPart('diaSemana');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pedido.detail', {
                parent: 'entity',
                url: '/pedido/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.pedido.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pedido/pedido-detail.html',
                        controller: 'PedidoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pedido');
                        $translatePartialLoader.addPart('diaSemana');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Pedido', function($stateParams, Pedido) {
                        return Pedido.get({id : $stateParams.id});
                    }]
                }
            })
            .state('pedido.new', {
                parent: 'pedido',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pedido/pedido-dialog.html',
                        controller: 'PedidoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    dtPrevistaSeparacao: null,
                                    dtRealSeparacao: null,
                                    dtPrevistaEntrega: null,
                                    dtRealEntrega: null,
                                    periodoPedidoInicio: null,
                                    periodoPedidoFim: null,
                                    diaSemana: null,
                                    dataPedido: null,
                                    usuarioQueFezPedido: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('pedido', null, { reload: true });
                    }, function() {
                        $state.go('pedido');
                    })
                }]
            })
            .state('pedido.edit', {
                parent: 'pedido',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pedido/pedido-dialog.html',
                        controller: 'PedidoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Pedido', function(Pedido) {
                                return Pedido.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pedido', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
