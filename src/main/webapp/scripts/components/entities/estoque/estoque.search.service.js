'use strict';

angular.module('controlesApp')
    .factory('EstoqueSearch', function ($resource) {
        return $resource('api/_search/estoques/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
