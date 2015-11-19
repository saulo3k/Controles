'use strict';

angular.module('controlesApp')
    .factory('Estoque', function ($resource, DateUtils) {
        return $resource('api/estoques/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dataAtual = DateUtils.convertLocaleDateFromServer(data.dataAtual);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dataAtual = DateUtils.convertLocaleDateToServer(data.dataAtual);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dataAtual = DateUtils.convertLocaleDateToServer(data.dataAtual);
                    return angular.toJson(data);
                }
            }
        });
    });
