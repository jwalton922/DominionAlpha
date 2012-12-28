'use strict';

function DominionController($scope, $log, $http, $rootScope) {
    $scope.frameCount = 0;
    $log.log("dominion controller called");
    
    $scope.incrFrameCount = function(){
        $log.log("incrementing frame count. before incr value "+$scope.frameCount);
        $scope.frameCount = $scope.frameCount+1;
    }
}