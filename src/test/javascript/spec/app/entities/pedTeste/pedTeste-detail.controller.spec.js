'use strict';

describe('PedTeste Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockPedTeste, MockProduto;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockPedTeste = jasmine.createSpy('MockPedTeste');
        MockProduto = jasmine.createSpy('MockProduto');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'PedTeste': MockPedTeste,
            'Produto': MockProduto
        };
        createController = function() {
            $injector.get('$controller')("PedTesteDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'controlesApp:pedTesteUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
