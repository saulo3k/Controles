'use strict';

describe('Pedido Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPedido, MockProduto, MockUser, MockCliente;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPedido = jasmine.createSpy('MockPedido');
        MockProduto = jasmine.createSpy('MockProduto');
        MockUser = jasmine.createSpy('MockUser');
        MockCliente = jasmine.createSpy('MockCliente');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Pedido': MockPedido,
            'Produto': MockProduto,
            'User': MockUser,
            'Cliente': MockCliente
        };
        createController = function() {
            $injector.get('$controller')("PedidoDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'controlesApp:pedidoUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
