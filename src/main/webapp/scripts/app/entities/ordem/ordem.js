'use strict';

angular.module('controlesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ordem', {
                parent: 'entity',
                url: '/ordems',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.ordem.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ordem/ordems.html',
                        controller: 'OrdemController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ordem');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('ordem.detail', {
                parent: 'entity',
                url: '/ordem/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.ordem.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ordem/ordem-detail.html',
                        controller: 'OrdemDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ordem');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Ordem', function($stateParams, Ordem) {
                        return Ordem.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ordem.new', {
                parent: 'ordem',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ordem/ordem-dialog.html',
                        controller: 'OrdemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    periodoPedidoInicio: null,
                                    periodoPedidoFim: null,
                                    dtPrevistaSeparacao: null,
                                    dtRealSeparacao: null,
                                    dtPrevistaEntrega: null,
                                    dtRealEntrega: null,
                                    dataPedido: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('ordem', null, { reload: true });
                    }, function() {
                        $state.go('ordem');
                    })
                }]
            })
            .state('ordem.edit', {
                parent: 'ordem',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/ordem/ordem-dialog.html',
                        controller: 'OrdemDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Ordem', function(Ordem) {
                                return Ordem.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ordem', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
