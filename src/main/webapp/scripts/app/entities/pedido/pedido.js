'use strict';

angular.module('controlesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pedido', {
                parent: 'entity',
                url: '/pedidos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.pedido.home.titleEntrega'
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
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })            
            .state('pedido.separacao', {
                parent: 'entity',
                url: '/pedidos-separacao',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.pedido.home.titleSeparacao'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pedido/pedidos-separacao.html',
                        controller: 'PedidoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pedido');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pedido.entrega', {
                parent: 'entity',
                url: '/pedidos-entrega',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.pedido.home.titleEntrega'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pedido/pedidos-entrega.html',
                        controller: 'PedidoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pedido');
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
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pedido/pedido-create.html',
                        controller: 'PedidoDialogController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pedido');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pedido.new1', {
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
                                    dataPedido: null,
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
