'use strict';

angular.module('controlesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('estoque', {
                parent: 'entity',
                url: '/estoques',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.estoque.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/estoque/estoques.html',
                        controller: 'EstoqueController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('estoque');
                        $translatePartialLoader.addPart('operacaoEstoque');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('estoque.detail', {
                parent: 'entity',
                url: '/estoque/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.estoque.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/estoque/estoque-detail.html',
                        controller: 'EstoqueDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('estoque');
                        $translatePartialLoader.addPart('operacaoEstoque');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Estoque', function($stateParams, Estoque) {
                        return Estoque.get({id : $stateParams.id});
                    }]
                }
            })
            .state('estoque.new', {
                parent: 'estoque',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/estoque/estoque-dialog.html',
                        controller: 'EstoqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    quantidadeAtual: null,
                                    quantidadeAposMovimentacao: null,
                                    dataAtual: null,
                                    operacao: null,
                                    motivo: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('estoque', null, { reload: true });
                    }, function() {
                        $state.go('estoque');
                    })
                }]
            })
            .state('estoque.edit', {
                parent: 'estoque',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/estoque/estoque-dialog.html',
                        controller: 'EstoqueDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Estoque', function(Estoque) {
                                return Estoque.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('estoque', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
