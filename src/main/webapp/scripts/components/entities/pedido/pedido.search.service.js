'use strict';

angular.module('controlesApp')
    .factory('PedidoSearch', function ($resource) {
        return $resource('api/_search/pedidos/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
