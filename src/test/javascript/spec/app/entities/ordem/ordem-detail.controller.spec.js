'use strict';

describe('Ordem Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockOrdem, MockProduto, MockCliente, MockUser;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockOrdem = jasmine.createSpy('MockOrdem');
        MockProduto = jasmine.createSpy('MockProduto');
        MockCliente = jasmine.createSpy('MockCliente');
        MockUser = jasmine.createSpy('MockUser');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Ordem': MockOrdem,
            'Produto': MockProduto,
            'Cliente': MockCliente,
            'User': MockUser
        };
        createController = function() {
            $injector.get('$controller')("OrdemDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'controlesApp:ordemUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
