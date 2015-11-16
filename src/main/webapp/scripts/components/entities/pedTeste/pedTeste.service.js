'use strict';

angular.module('controlesApp')
    .factory('PedTeste', function ($resource, DateUtils) {
        return $resource('api/pedTestes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dtPrevistaEntrega = DateUtils.convertLocaleDateFromServer(data.dtPrevistaEntrega);
                    data.dtPrevistaEntregaZoned = DateUtils.convertDateTimeFromServer(data.dtPrevistaEntregaZoned);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dtPrevistaEntrega = DateUtils.convertLocaleDateToServer(data.dtPrevistaEntrega);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dtPrevistaEntrega = DateUtils.convertLocaleDateToServer(data.dtPrevistaEntrega);
                    return angular.toJson(data);
                }
            }
        });
    });
