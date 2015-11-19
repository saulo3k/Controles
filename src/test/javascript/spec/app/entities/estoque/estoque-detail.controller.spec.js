'use strict';

describe('Estoque Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockEstoque, MockProduto, MockUser;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockEstoque = jasmine.createSpy('MockEstoque');
        MockProduto = jasmine.createSpy('MockProduto');
        MockUser = jasmine.createSpy('MockUser');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Estoque': MockEstoque,
            'Produto': MockProduto,
            'User': MockUser
        };
        createController = function() {
            $injector.get('$controller')("EstoqueDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'controlesApp:estoqueUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
