'use strict';

angular.module('controlesApp')
    .factory('ProdutoSearch', function ($resource) {
        return $resource('api/_search/produtos/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
