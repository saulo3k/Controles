'use strict';

angular.module('controlesApp')
.factory('Quantidade', function ($resource) {
    return $resource('api/quantidade/:id', {}, {    	
        'query': { method: 'GET'},
        'get': {
            method: 'GET',
            transformResponse: function (data) {
                data = angular.fromJson(data);                
                return data;
            }
        }        
    });
});
