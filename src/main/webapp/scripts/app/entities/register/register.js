'use strict';

angular.module('controlesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('register', {
                parent: 'admin',
                url: '/register2',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'register.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/register/register.html',
                        controller: 'RegisterController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('register');
                        return $translate.refresh();
                    }]
                }
            });
    });
