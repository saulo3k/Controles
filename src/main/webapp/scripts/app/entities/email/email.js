'use strict';

angular.module('controlesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('email', {
                parent: 'entity',
                url: '/emails',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.email.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/email/emails.html',
                        controller: 'EmailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('email');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('email.detail', {
                parent: 'entity',
                url: '/email/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'controlesApp.email.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/email/email-detail.html',
                        controller: 'EmailDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('email');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Email', function($stateParams, Email) {
                        return Email.get({id : $stateParams.id});
                    }]
                }
            })
            .state('email.new', {
                parent: 'email',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/email/email-dialog.html',
                        controller: 'EmailDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('email', null, { reload: true });
                    }, function() {
                        $state.go('email');
                    })
                }]
            })
            .state('email.edit', {
                parent: 'email',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/email/email-dialog.html',
                        controller: 'EmailDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Email', function(Email) {
                                return Email.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('email', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
