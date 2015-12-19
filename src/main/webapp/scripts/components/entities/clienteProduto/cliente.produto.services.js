'use strict';

angular.module('controlesApp')
.factory('ClienteProduto', function ($resource) {
    return $resource('api/clientes/produtos/:id', {}, {    	
        'query': { method: 'GET', isArray: true},
        'get': { method: 'GET', isArray: true},        
        'update': {                 
        	method: 'PUT',
        	transformRequest: function (data) {
        		return angular.toJson(data);
        	}   
       }
    });
});
