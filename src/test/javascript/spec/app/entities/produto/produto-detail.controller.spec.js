'use strict';

describe('Produto Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockProduto, MockCategoriaProduto, MockUser;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockProduto = jasmine.createSpy('MockProduto');
        MockCategoriaProduto = jasmine.createSpy('MockCategoriaProduto');
        MockUser = jasmine.createSpy('MockUser');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'Produto': MockProduto,
            'CategoriaProduto': MockCategoriaProduto,
            'User': MockUser
        };
        createController = function() {
            $injector.get('$controller')("ProdutoDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'controlesApp:produtoUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
