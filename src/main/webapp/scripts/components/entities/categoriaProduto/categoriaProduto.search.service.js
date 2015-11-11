'use strict';

angular.module('controlesApp')
    .factory('CategoriaProdutoSearch', function ($resource) {
        return $resource('api/_search/categoriaProdutos/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
