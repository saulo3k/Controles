'use strict';

angular.module('controlesApp')
    .factory('OrdemSearch', function ($resource) {
        return $resource('api/_search/ordems/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
