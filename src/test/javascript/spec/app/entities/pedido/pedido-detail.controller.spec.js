'use strict';

describe('Pedido Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPedido, MockProduto, MockUser;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPedido = jasmine.createSpy('MockPedido');
        MockProduto = jasmine.createSpy('MockProduto');
        MockUser = jasmine.createSpy('MockUser');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Pedido': MockPedido,
            'Produto': MockProduto,
            'User': MockUser
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
