(function (angular) {
    'use strict';
    function printDirective() {
    	
        var printSection = document.getElementById('printSection');       
        // if there is no printing section, create one
        if (!printSection) {        	
            printSection = document.createElement('div');
            printSection.id = 'printSection';
            console.log('printsection2',printSection.innerHTML);
            document.body.appendChild(printSection);
        }
        function link(scope, element, attrs) {
            element.on('click', function () {
                var elemToPrint = document.getElementById(attrs.printElementId);

                if (elemToPrint) {
                    printElement(elemToPrint);
                }
            });            
            window.onafterprint = function () {
                // clean the print section before adding new content                               
                printSection.innerHTML = '';
            }
        }
        function printElement(elem) {
            // clones the element you want to print
            var domClone = elem.cloneNode(true);
            printSection.appendChild(domClone);
            window.print();
            console.log('printsect');
        }
        return {
            link: link,
            restrict: 'A'
        };
    }
    angular.module('controlesApp').directive('ngPrint', [printDirective]);
}(window.angular));