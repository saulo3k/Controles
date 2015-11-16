'use strict';

angular.module('controlesApp')
    .factory('PedTesteSearch', function ($resource) {
        return $resource('api/_search/pedTestes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
