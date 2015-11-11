'use strict';

angular.module('controlesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('categoriaProduto', {
                parent: 'entity',
                url: '/categoriaProdutos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.categoriaProduto.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/categoriaProduto/categoriaProdutos.html',
                        controller: 'CategoriaProdutoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('categoriaProduto');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('categoriaProduto.detail', {
                parent: 'entity',
                url: '/categoriaProduto/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.categoriaProduto.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/categoriaProduto/categoriaProduto-detail.html',
                        controller: 'CategoriaProdutoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('categoriaProduto');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'CategoriaProduto', function($stateParams, CategoriaProduto) {
                        return CategoriaProduto.get({id : $stateParams.id});
                    }]
                }
            })
            .state('categoriaProduto.new', {
                parent: 'categoriaProduto',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/categoriaProduto/categoriaProduto-dialog.html',
                        controller: 'CategoriaProdutoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nome: null,
                                    descricao: null,
                                    ativa: false,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('categoriaProduto', null, { reload: true });
                    }, function() {
                        $state.go('categoriaProduto');
                    })
                }]
            })
            .state('categoriaProduto.edit', {
                parent: 'categoriaProduto',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/categoriaProduto/categoriaProduto-dialog.html',
                        controller: 'CategoriaProdutoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CategoriaProduto', function(CategoriaProduto) {
                                return CategoriaProduto.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('categoriaProduto', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
