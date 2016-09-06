/**
 * Created by ParkJun on 2016-09-05.
 */
var myApp = angular.module('myRegi', []);
myApp.controller('SearchCtrl', ['$scope', '$http', '$window', function($scope, $http, $window) {
    $scope.search = function() {
        $scope.BT = $scope.searchBT.toString()
        $scope.result = $scope.BT + "의 검색결과\n";
        $scope.ref = "0";
        var reg = /[가-힣a-zA-Z]+#[0-9]{4,5}/;
        if(!reg.test($scope.BT)) {
            $scope.result += "올바르지 않은 배틀태그 형식입니다\n";
            return;
        }
        $scope.url = "http://localhost:8080/register";
        $http.post($scope.url, $scope.BT).success(function(data, status) {
            $scope.status = status;
            //$scope.data = data;
            $scope.ref = 1; //등록버튼 생성
            $scope.wsUrl = "ws://localhost:8080"+data.postfix; //접속할 웹소켓 주소
            $scope.wsID = data["id"]; //웹소켓 접속 ID
            $scope.btlevel = data.level;
            $scope.btrank = data.rank;
            $scope.result += "level : " + $scope.btlevel + "    rank : " + $scope.btrank +"\n";
            $scope.most = data.most1;
            /*for(var i = 0; i<3; i++) {
                $scope.result += data.most1[i].hero + " : " + data.most1[i].time + "\n";
            }*/
        }).error(function(data, status) {
            $scope.data = data || "Request Failed";
            $scope.status = status;
            $scope.result += "없는 계정명이거나 네트워크상의 문제로 검색이 지연중입니다.";
        });
    }
    $scope.registerWS = function() {
        $scope.messages = [];
        var ws = $window.ws = new WebSocket($scope.wsUrl);

    }
}]);
