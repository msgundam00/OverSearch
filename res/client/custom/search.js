/**
 * Created by ParkJun on 2016-09-09.
 */
var myApp = angular.module('myTeamSearch', []);
myApp.controller('SearchCtrl', ['$scope', '$http', function($scope, $http) {
    $scope.scoreRange = ["2000-", "2000+", "2200+", "2400+", "2600+", "2800+", "3000+", "3200+", "3400+", "3600+", "3800+", "4000+"];
    $scope.playHeroType = ["죽지 않는 탱커", "딜 잘 넣는 딜러", "슈퍼세이브 힐러"];
    $scope.search = function() {
        $scope.sendingData = {
            "rankRange" : $scope.searchMMR,
            "useMike" : $scope.usingMike,
            "playType" : $scope.playType
        };
        $scope.url = "http://localhost:8080/search";
        $http.post($scope.url, $scope.sendingData)
            .success(function(data, status) {

        }).error(function(data, status) {

        });
    }

}]);