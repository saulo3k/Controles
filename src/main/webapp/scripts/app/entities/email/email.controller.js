'use strict';

angular.module('controlesApp')
    .controller('EmailController', function ($scope, Email, EmailSearch, ParseLinks) {
        $scope.emails = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Email.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.emails.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.emails = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Email.get({id: id}, function(result) {
                $scope.email = result;
                $('#deleteEmailConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Email.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteEmailConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            EmailSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.emails = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.email = {
                id: null
            };
        };
    });
