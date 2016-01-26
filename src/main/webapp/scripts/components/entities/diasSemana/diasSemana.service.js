'use strict';

angular.module('controlesApp')
.factory('DiasSemana', function ($resource, DateUtils) {
    return $resource('api/diasSemana', {}, {     
        'query': { method: 'GET', isArray: true},
        'get': {
            method: 'GET',
            transformResponse: function (data) {
                data = angular.fromJson(data);
                return data;
            }
        }
    });
});