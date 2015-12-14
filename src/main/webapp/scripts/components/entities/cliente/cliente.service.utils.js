'use strict';

angular.module('controlesApp')
    .factory('ClienteQuantidade', function ($resource, DateUtils) {
        return $resource('api/clientes/quantidade', {}, {        	
            'query': { method: 'GET'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {                	
//                    data = angular.fromJson(data);                	
                    return Number(data);
                }
            },
            'update': { method:'PUT' }            
        });
    });
