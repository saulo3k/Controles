'use strict';

angular.module('controlesApp')
    .factory('Produto', function ($resource, DateUtils) {
        return $resource('api/produtos/:id', {}, {
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
