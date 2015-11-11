'use strict';

describe('CategoriaProduto Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockCategoriaProduto;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockCategoriaProduto = jasmine.createSpy('MockCategoriaProduto');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'CategoriaProduto': MockCategoriaProduto
        };
        createController = function() {
            $injector.get('$controller')("CategoriaProdutoDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'controlesApp:categoriaProdutoUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
