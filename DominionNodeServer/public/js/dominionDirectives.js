'use strict';

var dominionDirectives = angular.module('DominionDirectives', []);

dominionDirectives.directive('dominioncanvas', [function() {
        console.log("inside dominion canvas directive");
        return { link: function(scope, element, attrs) {
                var context = element[0].getContext('2d');
                console.log("inside directive framecount = "+scope.frameCount);
                scope.$watch('frameCount', function(newValue, oldValue) {
                    context.clearRect(0, 0, 1000, 800);
                    console.log("framecount = " + newValue);
                    context.beginPath();
                    context.arc(100 + newValue, 100, 15, 0, 2 * Math.PI, false);
                    context.fillStyle = 'red';
                    context.fill();
                    context.lineWidth = 1;
                    context.strokeStyle = '#003300';
                    context.stroke();

                }, true);
            }
        }
    }
]);

// angular.widget('canvas', function(compileElement) {
//
//       return function(linkElement) {
//           var scope = this,
//               board = linkElement[0].getContext('2d'),
//               height = 15;
//               
//           scope.$watch(scope.frameCount, function(newValue, oldValue) {
//               console.log(newValue);
//               
//           });           
//       };
//    });

