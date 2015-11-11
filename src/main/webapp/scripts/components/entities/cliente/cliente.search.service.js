'use strict';

angular.module('controlesApp')
    .factory('ClienteSearch', function ($resource) {
        return $resource('api/_search/clientes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
