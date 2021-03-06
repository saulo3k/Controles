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
            .state('pedido.separacao.listedit', {
                parent: 'pedido',
                url: '/editlist-separacao',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pedido/pedido-dialog-list-separacao.html',
                        controller: 'PedidoSeparacaoListDialogController',
                        windowClass: 'app-modal-window',
                        backdrop : 'static',
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
                        $state.go('pedido.separacao.listedit', null, { reload: true });
                    }, function() {                    	
                        $state.go('pedido', null, { reload: true });
                    })
                }]
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
                        $state.go('pedido.separacao', null, { reload: true });
                    }, function() {
                        $state.go('pedido.separacao', null, { reload: true });
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
//            .state('pedido.entrega.edit', {
//            	parent: 'pedido.entrega',
//                url: '/{id}/edit-entrega',
//                data: {
//                    authorities: ['ROLE_USER'],
//                },
//                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
//                    $modal.open({
//                        templateUrl: 'scripts/app/entities/pedido/pedido-dialog-entrega.html',
//                        controller: 'PedidoEntregaDialogController',
//                        size: 'lg',
//                        resolve: {
//                            entity: ['Pedido', function(Pedido) {
//                                return Pedido.get({id : $stateParams.id});
//                            }]
//                        }
//                    }).result.then(function(result) {
//                        $state.go('pedido.entrega', null, { reload: true });
//                    }, function() {
//                        $state.go('pedido.entrega');
//                    })
//                }]
//            })
            .state('pedido.imprimir', {
            	parent: 'entity',
                url: '/pedido-imprimir/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.pedido.home.titleEntrega'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pedido/pedido-print.html',
                        controller: 'PedidoEntregaDialogController'
                    }
                },
                resolve: {                	
                	translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pedido');
                        return $translate.refresh();
                    }],
                	entity: ['$stateParams', 'Pedido', function($stateParams, Pedido) {                		
                			return $stateParams.id;
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
                        backdrop : 'static',
                        resolve: {
                            entity: function () {
                            	var dataHoje = new Date();
                            	var dataAmanha = new Date();
                            	dataAmanha.setDate(dataHoje.getDate() + 1);
                                return {
                                    dtPrevistaSeparacao: dataHoje,
                                    dtRealSeparacao: null,
                                    dtPrevistaEntrega: dataAmanha,
                                    dtRealEntrega: null,
                                    periodoPedidoInicio: null,
                                    periodoPedidoFim: null,
                                    dataPedido: null,
                                	produtosPedidos: [],
                                	dataPedido: dataHoje,
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
            .state('pedido.newcliente', {
                parent: 'pedido',
                url: '/new-cliente',
                data: {
                    authorities: ['ROLE_USER'],
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('cliente');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
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
                        $state.go('pedido.new', null, { reload: true });
                    }, function() {
                    	console.log($modal);
                        $state.go('pedido.new', null, { reload: true });
                    })
                }]
            })
           .state('pedido.modelo', {
                parent: 'pedido',
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
                        backdrop : 'static',
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
            .state('pedido.edit.equa', {
                parent: 'pedido.separacao.listedit',
                url: '/{id}/editEqualizacao',
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
                        $state.go('pedido.separacao.listedit', result, { reload: true });
                    }, function() {
                    	$state.go('pedido.separacao.listedit');
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
                parent: 'pedido.modelo',
                url: '/{id}/editModel',
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
                        $state.go('pedido.modelo', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
