'use strict';

angular.module('controlesApp')
.factory('PedidoSeparacao', function ($resource) {
    return $resource('api/separacao', {}, {       
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
        }
    });
});