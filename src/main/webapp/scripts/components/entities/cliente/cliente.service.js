'use strict';

angular.module('controlesApp')
    .factory('Cliente', function ($resource, DateUtils) {
        return $resource('api/clientes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }            
        });
    });
