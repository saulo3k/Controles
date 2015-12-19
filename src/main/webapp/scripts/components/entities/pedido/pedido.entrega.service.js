'use strict';

angular.module('controlesApp')
.factory('PedidoEntrega', function ($resource, DateUtils) {
    return $resource('api/entregas', {}, {     
        'query': { method: 'GET', isArray: true},
        'get': {
            method: 'GET',
            transformResponse: function (data) {
                data = angular.fromJson(data);
                data.dtPrevistaSeparacao = DateUtils.convertLocaleDateFromServer(data.dtPrevistaSeparacao);
                data.dtRealSeparacao = DateUtils.convertLocaleDateFromServer(data.dtRealSeparacao);
                data.dtPrevistaEntrega = DateUtils.convertLocaleDateFromServer(data.dtPrevistaEntrega);
                data.dtRealEntrega = DateUtils.convertLocaleDateFromServer(data.dtRealEntrega);
                data.periodoPedidoInicio = DateUtils.convertLocaleDateFromServer(data.periodoPedidoInicio);
                data.periodoPedidoFim = DateUtils.convertLocaleDateFromServer(data.periodoPedidoFim);
                data.dataPedido = DateUtils.convertLocaleDateFromServer(data.dataPedido);
                return data;
            }
        },
        'updateEntrega': {
            method: 'PUT',
            transformRequest: function (data) {
                data.dtPrevistaSeparacao = DateUtils.convertLocaleDateToServer(data.dtPrevistaSeparacao);
                data.dtRealSeparacao = DateUtils.convertLocaleDateToServer(data.dtRealSeparacao);
                data.dtPrevistaEntrega = DateUtils.convertLocaleDateToServer(data.dtPrevistaEntrega);
                data.dtRealEntrega = DateUtils.convertLocaleDateToServer(data.dtRealEntrega);
                data.periodoPedidoInicio = DateUtils.convertLocaleDateToServer(data.periodoPedidoInicio);
                data.periodoPedidoFim = DateUtils.convertLocaleDateToServer(data.periodoPedidoFim);
                data.dataPedido = DateUtils.convertLocaleDateToServer(data.dataPedido);
                return angular.toJson(data);
            }
        },
    });
});