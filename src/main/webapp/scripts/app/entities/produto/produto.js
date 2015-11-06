'use strict';

angular.module('controlesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('produto', {
                parent: 'entity',
                url: '/produtos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.produto.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/produto/produtos.html',
                        controller: 'ProdutoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('produto');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('produto.detail', {
                parent: 'entity',
                url: '/produto/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.produto.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/produto/produto-detail.html',
                        controller: 'ProdutoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('produto');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Produto', function($stateParams, Produto) {
                        return Produto.get({id : $stateParams.id});
                    }]
                }
            })
            .state('produto.new', {
                parent: 'produto',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/produto/produto-dialog.html',
                        controller: 'ProdutoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nome: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('produto', null, { reload: true });
                    }, function() {
                        $state.go('produto');
                    })
                }]
            })
            .state('produto.edit', {
                parent: 'produto',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/produto/produto-dialog.html',
                        controller: 'ProdutoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Produto', function(Produto) {
                                return Produto.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('produto', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
