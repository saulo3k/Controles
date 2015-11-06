'use strict';

angular.module('controlesApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


