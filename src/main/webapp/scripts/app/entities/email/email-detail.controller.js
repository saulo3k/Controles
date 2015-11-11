'use strict';

angular.module('controlesApp')
    .controller('EmailDetailController', function ($scope, $rootScope, $stateParams, entity, Email) {
        $scope.email = entity;
        $scope.load = function (id) {
            Email.get({id: id}, function(result) {
                $scope.email = result;
            });
        };
        var unsubscribe = $rootScope.$on('controlesApp:emailUpdate', function(event, result) {
            $scope.email = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
