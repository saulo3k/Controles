'use strict';

angular.module('controlesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pedTeste', {
                parent: 'entity',
                url: '/pedTestes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.pedTeste.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pedTeste/pedTestes.html',
                        controller: 'PedTesteController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pedTeste');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('pedTeste.detail', {
                parent: 'entity',
                url: '/pedTeste/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.pedTeste.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pedTeste/pedTeste-detail.html',
                        controller: 'PedTesteDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pedTeste');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PedTeste', function($stateParams, PedTeste) {
                        return PedTeste.get({id : $stateParams.id});
                    }]
                }
            })
            .state('pedTeste.new', {
                parent: 'pedTeste',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pedTeste/pedTeste-dialog.html',
                        controller: 'PedTesteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    dtPrevistaEntrega: null,
                                    dtPrevistaEntregaZoned: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('pedTeste', null, { reload: true });
                    }, function() {
                        $state.go('pedTeste');
                    })
                }]
            })
            .state('pedTeste.edit', {
                parent: 'pedTeste',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pedTeste/pedTeste-dialog.html',
                        controller: 'PedTesteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PedTeste', function(PedTeste) {
                                return PedTeste.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('pedTeste', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
