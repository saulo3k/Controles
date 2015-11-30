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
            .state('pedido.separacao.edit', {
                parent: 'pedido.separacao',
                url: '/{id}/edit-separacao',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pedido/pedido-dialog-separacao.html',
                        controller: 'PedidoSeparacaoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Pedido', function(Pedido) {
                                return Pedido.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pedido', null, { reload: true });
                    }, function() {
                        $state.go('pedido');
                    })
                }]
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
                        controller: 'PedidoSeparacaoController'
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
                        controller: 'PedidoEntregaController'
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
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pedido/pedido-dialog.html',
                        controller: 'PedidoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    dtPrevistaSeparacao: new Date(),
                                    dtRealSeparacao: null,
                                    dtPrevistaEntrega: new Date(),
                                    dtRealEntrega: null,
                                    periodoPedidoInicio: null,
                                    periodoPedidoFim: null,
                                    dataPedido: null,
                                	produtosPedidos: [],
                                	dataPedido: new Date(),
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
           .state('pedido.modelo', {
                parent: 'entity',
                url: '/pedidos-modelo',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.pedido.home.titlemodelo'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pedido/pedidos-modelo.html',
                        controller: 'PedidoModController'
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
           .state('pedido.modelo.new', {
                parent: 'pedido.modelo',
                url: '/model',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pedido/pedido-dialog-model.html',
                        controller: 'PedidoDialogModelController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    dtPrevistaSeparacao: new Date(),
                                    dtRealSeparacao: null,
                                    dtPrevistaEntrega: new Date(),
                                    dtRealEntrega: null,
                                    periodoPedidoInicio: null,
                                    periodoPedidoFim: null,
                                    dataPedido: null,
                                	produtosPedidos: [],
                                	dataPedido: new Date(),
                                	pedidoModelo: 1,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('pedido.modelo', null, { reload: true });
                    }, function() {
                        $state.go('pedido.modelo');
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
            })
            .state('pedido.edit.model', {
                parent: 'pedido',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pedido/pedido-dialog-model.html',
                        controller: 'PedidoDialogModelController',
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
