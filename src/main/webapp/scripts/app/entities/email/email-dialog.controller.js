'use strict';

angular.module('controlesApp').controller('EmailDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Email',
        function($scope, $stateParams, $modalInstance, entity, Email) {

        $scope.email = entity;
        $scope.load = function(id) {
            Email.get({id : id}, function(result) {
                $scope.email = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('controlesApp:emailUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.email.id != null) {
                Email.update($scope.email, onSaveFinished);
            } else {
                Email.save($scope.email, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
